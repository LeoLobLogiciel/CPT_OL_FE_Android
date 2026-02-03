package ar.com.logiciel.cptmobile.domain.usecase

import ar.com.logiciel.cptmobile.domain.model.User
import ar.com.logiciel.cptmobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> {
        return authRepository.getCurrentUser()
    }
}
