package ar.com.logiciel.cptmobile.presentation.splash

import androidx.lifecycle.ViewModel
import ar.com.logiciel.cptmobile.domain.usecase.IsLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ViewModel() {

    val isLoggedIn: Flow<Boolean> = isLoggedInUseCase()
}
