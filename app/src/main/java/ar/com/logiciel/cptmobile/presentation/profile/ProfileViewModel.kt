package ar.com.logiciel.cptmobile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.logiciel.cptmobile.domain.model.User
import ar.com.logiciel.cptmobile.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for the Profile/Configuration screen.
 * Handles logout functionality.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog.asStateFlow()

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut: StateFlow<Boolean> = _isLoggingOut.asStateFlow()

    init {
        Timber.d("👤 ProfileViewModel initialized")
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                _currentUser.value = user
                Timber.d("👤 Current user loaded: ${user?.fullName}")
            }
        }
    }

    fun showLogoutDialog() {
        Timber.d("🚪 Showing logout confirmation dialog")
        _showLogoutDialog.value = true
    }

    fun dismissLogoutDialog() {
        Timber.d("🚪 Dismissing logout confirmation dialog")
        _showLogoutDialog.value = false
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _isLoggingOut.value = true
                Timber.d("🚪 Logging out user...")
                Timber.d("   - Clearing DataStore preferences")
                Timber.d("   - Removing user token")

                authRepository.logout()

                Timber.d("✅ Logout successful")
                Timber.d("   - User will be redirected to login screen")
            } catch (e: Exception) {
                Timber.e("❌ Error during logout: ${e.message}")
            } finally {
                _isLoggingOut.value = false
                _showLogoutDialog.value = false
            }
        }
    }
}
