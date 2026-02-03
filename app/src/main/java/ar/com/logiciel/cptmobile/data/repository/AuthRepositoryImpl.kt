package ar.com.logiciel.cptmobile.data.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.data.local.PreferencesManager
import ar.com.logiciel.cptmobile.data.remote.api.AuthApi
import ar.com.logiciel.cptmobile.data.remote.dto.LoginRequest
import ar.com.logiciel.cptmobile.data.remote.dto.toDomainModel
import ar.com.logiciel.cptmobile.domain.model.User
import ar.com.logiciel.cptmobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): NetworkResult<User> {
        return try {
            Timber.d("📡 Llamando a API de login...")
            Timber.d("Endpoint: POST /auth/login")
            Timber.d("Request body: { username: \"$username\", password: \"***\" }")

            val response = authApi.login(
                LoginRequest(username = username, password = password)
            )

            Timber.d("📥 Respuesta recibida - HTTP ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!

                // Verificar el status de la respuesta
                if (loginResponse.status == "ERROR") {
                    Timber.e("❌ API retornó ERROR")
                    return NetworkResult.Error("Credenciales inválidas")
                }

                val user = loginResponse.toDomainModel()

                Timber.d("✅ Response exitoso:")
                Timber.d("   - Status: ${loginResponse.status}")
                Timber.d("   - Token: ${user.token.take(20)}...")

                // Save user data to preferences
                preferencesManager.saveUserToken(user.token)
                preferencesManager.saveUserId(user.id)
                user.email?.let { preferencesManager.saveUserEmail(it) }
                preferencesManager.saveUserName(user.fullName)
                preferencesManager.setLoggedIn(true)

                Timber.d("💾 Datos guardados en DataStore")
                NetworkResult.Success(user)
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Usuario inexistente o contraseña incorrecta"
                    403 -> "Tu usuario se encuentra temporalmente inactivo. Por favor, contactá a tu supervisor."
                    else -> "Error al iniciar sesión: ${response.message()}"
                }
                Timber.e("❌ Response con error HTTP ${response.code()}: $errorMessage")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("LSI:CredencialesDeUsuarioInvalidas") == true ->
                    "Usuario inexistente o contraseña incorrecta"
                e.message?.contains("LSI:UsuarioInactivo") == true ->
                    "Tu usuario se encuentra temporalmente inactivo. Por favor, contactá a tu supervisor."
                else -> "Error de conexión: ${e.localizedMessage}"
            }
            Timber.e("💥 Excepción durante login:")
            Timber.e("   Exception type: ${e.javaClass.simpleName}")
            Timber.e("   Message: ${e.message}")
            Timber.e("   Error final: $errorMessage")
            NetworkResult.Error(errorMessage)
        }
    }

    override suspend fun logout() {
        Timber.d("Logging out user")
        preferencesManager.clearAll()
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return preferencesManager.isLoggedIn
    }

    override fun getCurrentUser(): Flow<User?> {
        return combine(
            preferencesManager.userId,
            preferencesManager.userName,
            preferencesManager.userEmail,
            preferencesManager.userToken
        ) { userId, userName, userEmail, userToken ->
            if (userId != null && userToken != null) {
                User(
                    id = userId,
                    username = userName ?: "",
                    email = userEmail,
                    nombre = userName,
                    apellido = null,
                    token = userToken
                )
            } else {
                null
            }
        }
    }
}
