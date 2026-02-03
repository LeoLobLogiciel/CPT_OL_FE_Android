package ar.com.logiciel.cptmobile.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.User
import ar.com.logiciel.cptmobile.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class LoginUiState(
    val username: String = "leolob",
    val password: String = "cpt1789",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username,
            error = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            error = null
        )
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        if (currentState.username.isBlank()) {
            _uiState.value = currentState.copy(error = "El nombre de usuario es obligatorio")
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.value = currentState.copy(error = "La contraseña es obligatoria")
            return
        }

        login(currentState.username, currentState.password)
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            Timber.d("═══════════════════════════════════════")
            Timber.d("🔐 INICIANDO LOGIN")
            Timber.d("Usuario: $username")
            Timber.d("═══════════════════════════════════════")

            when (val result = loginUseCase(username, password)) {
                is NetworkResult.Success -> {
                    Timber.d("═══════════════════════════════════════")
                    Timber.d("✅ LOGIN EXITOSO")
                    Timber.d("Usuario: ${result.data?.nombre}")
                    Timber.d("ID: ${result.data?.id}")
                    Timber.d("Token guardado correctamente")
                    Timber.d("═══════════════════════════════════════")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loginSuccess = true,
                        error = null
                    )
                }

                is NetworkResult.Error -> {
                    Timber.e("═══════════════════════════════════════")
                    Timber.e("❌ LOGIN FALLIDO")
                    Timber.e("Error: ${result.message}")
                    Timber.e("═══════════════════════════════════════")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message ?: "Error desconocido"
                    )
                }

                is NetworkResult.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
