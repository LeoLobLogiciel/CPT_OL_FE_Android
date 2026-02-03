package ar.com.logiciel.cptmobile.domain.usecase

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.User
import ar.com.logiciel.cptmobile.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): NetworkResult<User> {
        if (username.isBlank()) {
            return NetworkResult.Error("El nombre de usuario es obligatorio")
        }

        if (password.isBlank()) {
            return NetworkResult.Error("La contraseña es obligatoria")
        }

        return authRepository.login(username, password)
    }
}
