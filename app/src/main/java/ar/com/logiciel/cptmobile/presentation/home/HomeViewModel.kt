package ar.com.logiciel.cptmobile.presentation.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.logiciel.cptmobile.domain.model.User
import ar.com.logiciel.cptmobile.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for the Home screen.
 * Manages the navigation drawer state and the currently selected menu item.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _drawerState = MutableStateFlow(false)
    val drawerState: StateFlow<Boolean> = _drawerState.asStateFlow()

    private val _selectedMenuItem = MutableStateFlow<MenuItem>(MenuItem.ClientesTablero)
    val selectedMenuItem: StateFlow<MenuItem> = _selectedMenuItem.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()

    private companion object {
        val KEY_LAST_SCREEN = stringPreferencesKey("home_last_screen")
    }

    init {
        Timber.d("🏠 HomeViewModel initialized")
        loadLastScreen()
        loadCurrentUser()
        observeLoginState()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                _currentUser.value = user
                Timber.d("👤 Current user loaded: ${user?.fullName}")
            }
        }
    }

    private fun observeLoginState() {
        viewModelScope.launch {
            authRepository.isLoggedIn().collect { isLoggedIn ->
                if (!isLoggedIn) {
                    Timber.d("🚪 User logged out detected")
                    _isLoggedOut.value = true
                }
            }
        }
    }

    fun openDrawer() {
        Timber.d("📂 Opening navigation drawer")
        _drawerState.value = true
    }

    fun closeDrawer() {
        Timber.d("📂 Closing navigation drawer")
        _drawerState.value = false
    }

    fun selectMenuItem(item: MenuItem) {
        Timber.d("📍 Menu item selected: ${item.title} (${item.route})")
        _selectedMenuItem.value = item
        saveLastScreen(item)
        closeDrawer()
    }

    private fun loadLastScreen() {
        viewModelScope.launch {
            val preferences = dataStore.data.first()
            val savedRoute = preferences[KEY_LAST_SCREEN]
            if (savedRoute != null) {
                val menuItem = when (savedRoute) {
                    "pedidos" -> MenuItem.PedidosPanel
                    "clientes_panel" -> MenuItem.ClientesPanel
                    "tablero" -> MenuItem.ClientesTablero
                    "ventas" -> MenuItem.ClientesVentas
                    "utilitarios" -> MenuItem.Utilitarios
                    else -> MenuItem.ClientesTablero
                }
                _selectedMenuItem.value = menuItem
                Timber.d("📱 Última pantalla cargada: ${menuItem.title}")
            }
        }
    }

    private fun saveLastScreen(item: MenuItem) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[KEY_LAST_SCREEN] = item.route
            }
            Timber.d("💾 Pantalla guardada: ${item.title}")
        }
    }

    fun getCurrentScreenTitle(): String {
        val item = _selectedMenuItem.value
        return when (item) {
            is MenuItem.PedidosPanel -> "Pedidos"
            is MenuItem.ClientesPanel -> "Clientes y Ventas"
            is MenuItem.ClientesTablero -> "Tablero"
            is MenuItem.ClientesVentas -> "Ventas"
            is MenuItem.Utilitarios -> "Configuración"
            is MenuItem.Logout -> ""
        }
    }
}
