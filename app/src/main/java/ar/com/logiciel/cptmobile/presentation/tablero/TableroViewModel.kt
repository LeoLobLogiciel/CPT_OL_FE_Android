package ar.com.logiciel.cptmobile.presentation.tablero

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.tablero.VentaPorRubroData
import ar.com.logiciel.cptmobile.domain.repository.TableroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TableroViewModel @Inject constructor(
    private val tableroRepository: TableroRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(TableroUiState())
    val uiState: StateFlow<TableroUiState> = _uiState.asStateFlow()

    private var isLoadingFromStorage = false

    // Keys para DataStore
    private companion object {
        // Fechas Ventas Web
        val KEY_VENTAS_WEB_FECHA_DESDE = longPreferencesKey("tablero.ventasWeb.fechaDesde")
        val KEY_VENTAS_WEB_FECHA_HASTA = longPreferencesKey("tablero.ventasWeb.fechaHasta")
        
        // Fechas Ventas
        val KEY_VENTAS_FECHA_DESDE = longPreferencesKey("tablero.venta.fechaDesde")
        val KEY_VENTAS_FECHA_HASTA = longPreferencesKey("tablero.venta.fechaHasta")
        
        // Fechas Ventas Rubro
        val KEY_VENTAS_RUBRO_FECHA_DESDE = longPreferencesKey("tablero.ventasRubro.fechaDesde")
        val KEY_VENTAS_RUBRO_FECHA_HASTA = longPreferencesKey("tablero.ventasRubro.fechaHasta")
        
        // Estados de expansión
        val KEY_VENTAS_WEB_EXPANDED = booleanPreferencesKey("tablero.ventasWeb.isExpanded")
        val KEY_VENTAS_ZONA_EXPANDED = booleanPreferencesKey("tablero.venta.isExpanded")
        val KEY_DEUDA_CLIENTES_EXPANDED = booleanPreferencesKey("tablero.deudaClientes.isExpanded")
        val KEY_VENTAS_RUBRO_EXPANDED = booleanPreferencesKey("tablero.ventasRubro.isExpanded")
        val KEY_DEUDA_PROVEEDORES_EXPANDED = booleanPreferencesKey("tablero.deudaProveedores.isExpanded")
        val KEY_RESMAS_EXPANDED = booleanPreferencesKey("tablero.resmas.isExpanded")
    }

    init {
        loadPersistedData()
    }

    private fun loadPersistedData() {
        isLoadingFromStorage = true
        viewModelScope.launch {
            dataStore.data.first().let { prefs ->
                // Fechas
                val ventasWebDesde = prefs[KEY_VENTAS_WEB_FECHA_DESDE]?.let { LocalDate.ofEpochDay(it) } 
                    ?: LocalDate.now().withDayOfMonth(1)
                val ventasWebHasta = prefs[KEY_VENTAS_WEB_FECHA_HASTA]?.let { LocalDate.ofEpochDay(it) } 
                    ?: LocalDate.now()
                
                val ventasDesde = prefs[KEY_VENTAS_FECHA_DESDE]?.let { LocalDate.ofEpochDay(it) } 
                    ?: LocalDate.now().withDayOfMonth(1)
                val ventasHasta = prefs[KEY_VENTAS_FECHA_HASTA]?.let { LocalDate.ofEpochDay(it) } 
                    ?: LocalDate.now()
                
                val ventasRubroDesde = prefs[KEY_VENTAS_RUBRO_FECHA_DESDE]?.let { LocalDate.ofEpochDay(it) } 
                    ?: LocalDate.now().withDayOfMonth(1)
                val ventasRubroHasta = prefs[KEY_VENTAS_RUBRO_FECHA_HASTA]?.let { LocalDate.ofEpochDay(it) } 
                    ?: LocalDate.now()
                
                // Estados de expansión
                val ventasWebExpanded = prefs[KEY_VENTAS_WEB_EXPANDED] ?: true
                val ventasZonaExpanded = prefs[KEY_VENTAS_ZONA_EXPANDED] ?: true
                val deudaClientesExpanded = prefs[KEY_DEUDA_CLIENTES_EXPANDED] ?: true
                val ventasRubroExpanded = prefs[KEY_VENTAS_RUBRO_EXPANDED] ?: true
                val deudaProveedoresExpanded = prefs[KEY_DEUDA_PROVEEDORES_EXPANDED] ?: true
                val resmasExpanded = prefs[KEY_RESMAS_EXPANDED] ?: true
                
                _uiState.update {
                    it.copy(
                        ventasWebFechaDesde = ventasWebDesde,
                        ventasWebFechaHasta = ventasWebHasta,
                        ventasFechaDesde = ventasDesde,
                        ventasFechaHasta = ventasHasta,
                        ventasRubroFechaDesde = ventasRubroDesde,
                        ventasRubroFechaHasta = ventasRubroHasta,
                        ventasWebExpanded = ventasWebExpanded,
                        ventasZonaExpanded = ventasZonaExpanded,
                        deudaClientesExpanded = deudaClientesExpanded,
                        ventasRubroExpanded = ventasRubroExpanded,
                        deudaProveedoresExpanded = deudaProveedoresExpanded,
                        resmasExpanded = resmasExpanded
                    )
                }
                
                isLoadingFromStorage = false
                loadAllData()
            }
        }
    }

    fun loadAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            loadVentasWeb()
            loadVentas()
            loadDeudaClientes()
            loadVentasRubro()
            loadDeudaProveedores()
            loadResmas()
            
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun loadVentasWeb() {
        viewModelScope.launch {
            saveVentasWebDates()
            
            val state = _uiState.value
            val desde = state.ventasWebFechaDesde.toString()
            val hasta = state.ventasWebFechaHasta.toString()
            
            // Ventas Web ML
            when (val result = tableroRepository.getVentasWebML(desde, hasta)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(ventasWebML = result.data ?: emptyList()) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(ventasWebML = emptyList()) }
                }
                is NetworkResult.Loading -> {}
            }
            
            // Ventas Web Empresas
            when (val result = tableroRepository.getVentasWebEmpresas(desde, hasta)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(ventasWebEmpresas = result.data) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(ventasWebEmpresas = null) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun loadVentas() {
        viewModelScope.launch {
            saveVentasDates()
            
            val state = _uiState.value
            val desde = state.ventasFechaDesde.toString()
            val hasta = state.ventasFechaHasta.toString()
            
            when (val result = tableroRepository.getVentaPorZona(desde, hasta)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(ventasPorZona = result.data ?: emptyList()) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(ventasPorZona = emptyList()) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun loadDeudaClientes() {
        viewModelScope.launch {
            when (val result = tableroRepository.getDeudaClientes()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(deudaClientes = result.data ?: emptyList()) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(deudaClientes = emptyList()) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun loadVentasRubro() {
        viewModelScope.launch {
            saveVentasRubroDates()
            
            val state = _uiState.value
            val desde = state.ventasRubroFechaDesde.toString()
            val hasta = state.ventasRubroFechaHasta.toString()
            
            when (val result = tableroRepository.getVentaPorRubro(desde, hasta)) {
                is NetworkResult.Success -> {
                    val data = result.data
                    _uiState.update { 
                        it.copy(
                            ventasPorRubro = data?.ventaPorRubro ?: emptyList(),
                            ventaMLPorRubro = data?.ventaMLPorRubro ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { 
                        it.copy(
                            ventasPorRubro = emptyList(),
                            ventaMLPorRubro = emptyList()
                        )
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun loadDeudaProveedores() {
        viewModelScope.launch {
            when (val result = tableroRepository.getDeudaProveedores()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(deudaProveedores = result.data ?: emptyList()) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(deudaProveedores = emptyList()) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun loadResmas() {
        viewModelScope.launch {
            when (val result = tableroRepository.getResmas()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(resmas = result.data ?: emptyList()) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(resmas = emptyList()) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    // Setters de fecha
    fun setVentasWebFechaDesde(fecha: LocalDate) {
        _uiState.update { it.copy(ventasWebFechaDesde = fecha) }
    }

    fun setVentasWebFechaHasta(fecha: LocalDate) {
        _uiState.update { it.copy(ventasWebFechaHasta = fecha) }
    }

    fun setVentasFechaDesde(fecha: LocalDate) {
        _uiState.update { it.copy(ventasFechaDesde = fecha) }
    }

    fun setVentasFechaHasta(fecha: LocalDate) {
        _uiState.update { it.copy(ventasFechaHasta = fecha) }
    }

    fun setVentasRubroFechaDesde(fecha: LocalDate) {
        _uiState.update { it.copy(ventasRubroFechaDesde = fecha) }
    }

    fun setVentasRubroFechaHasta(fecha: LocalDate) {
        _uiState.update { it.copy(ventasRubroFechaHasta = fecha) }
    }

    // Setters de expansión
    fun setVentasWebExpanded(expanded: Boolean) {
        _uiState.update { it.copy(ventasWebExpanded = expanded) }
        saveExpandedState(KEY_VENTAS_WEB_EXPANDED, expanded)
    }

    fun setVentasZonaExpanded(expanded: Boolean) {
        _uiState.update { it.copy(ventasZonaExpanded = expanded) }
        saveExpandedState(KEY_VENTAS_ZONA_EXPANDED, expanded)
    }

    fun setDeudaClientesExpanded(expanded: Boolean) {
        _uiState.update { it.copy(deudaClientesExpanded = expanded) }
        saveExpandedState(KEY_DEUDA_CLIENTES_EXPANDED, expanded)
    }

    fun setVentasRubroExpanded(expanded: Boolean) {
        _uiState.update { it.copy(ventasRubroExpanded = expanded) }
        saveExpandedState(KEY_VENTAS_RUBRO_EXPANDED, expanded)
    }

    fun setDeudaProveedoresExpanded(expanded: Boolean) {
        _uiState.update { it.copy(deudaProveedoresExpanded = expanded) }
        saveExpandedState(KEY_DEUDA_PROVEEDORES_EXPANDED, expanded)
    }

    fun setResmasExpanded(expanded: Boolean) {
        _uiState.update { it.copy(resmasExpanded = expanded) }
        saveExpandedState(KEY_RESMAS_EXPANDED, expanded)
    }

    // Persistencia
    private fun saveVentasWebDates() {
        if (isLoadingFromStorage) return
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[KEY_VENTAS_WEB_FECHA_DESDE] = _uiState.value.ventasWebFechaDesde.toEpochDay()
                prefs[KEY_VENTAS_WEB_FECHA_HASTA] = _uiState.value.ventasWebFechaHasta.toEpochDay()
            }
        }
    }

    private fun saveVentasDates() {
        if (isLoadingFromStorage) return
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[KEY_VENTAS_FECHA_DESDE] = _uiState.value.ventasFechaDesde.toEpochDay()
                prefs[KEY_VENTAS_FECHA_HASTA] = _uiState.value.ventasFechaHasta.toEpochDay()
            }
        }
    }

    private fun saveVentasRubroDates() {
        if (isLoadingFromStorage) return
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[KEY_VENTAS_RUBRO_FECHA_DESDE] = _uiState.value.ventasRubroFechaDesde.toEpochDay()
                prefs[KEY_VENTAS_RUBRO_FECHA_HASTA] = _uiState.value.ventasRubroFechaHasta.toEpochDay()
            }
        }
    }

    private fun saveExpandedState(key: Preferences.Key<Boolean>, value: Boolean) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[key] = value
            }
        }
    }
}
