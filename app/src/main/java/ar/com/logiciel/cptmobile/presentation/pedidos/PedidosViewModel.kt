package ar.com.logiciel.cptmobile.presentation.pedidos

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.*
import ar.com.logiciel.cptmobile.domain.repository.ClientesRepository
import ar.com.logiciel.cptmobile.domain.repository.RubrosRepository
import ar.com.logiciel.cptmobile.domain.repository.VendedoresRepository
import ar.com.logiciel.cptmobile.domain.usecase.pedidos.GetPedidosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class PedidosViewModel @Inject constructor(
    private val getPedidosUseCase: GetPedidosUseCase,
    private val clientesRepository: ClientesRepository,
    private val vendedoresRepository: VendedoresRepository,
    private val rubrosRepository: RubrosRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidosUiState())
    val uiState: StateFlow<PedidosUiState> = _uiState.asStateFlow()

    private var isLoadingFromStorage = false

    // Keys para DataStore - TODOS los filtros se guardan
    private companion object {
        // Columnas
        val KEY_COLUMNAS_SELECCIONADAS = stringPreferencesKey("pedidos_columnas_seleccionadas")
        
        // Filtros - Fechas (ahora obligatorias)
        val KEY_FECHA_DESDE = longPreferencesKey("pedidos_fecha_desde")
        val KEY_FECHA_HASTA = longPreferencesKey("pedidos_fecha_hasta")
        
        // Filtros - Búsqueda y selecciones
        val KEY_SEARCH = stringPreferencesKey("pedidos_search")
        val KEY_VENDEDOR_ID = intPreferencesKey("pedidos_vendedor_id")
        val KEY_VENDEDOR_NOMBRE = stringPreferencesKey("pedidos_vendedor_nombre")
        val KEY_RUBROS_IDS = stringPreferencesKey("pedidos_rubros_ids")
        
        // Filtros - Estados booleanos
        val KEY_CONFIRMADOS = stringPreferencesKey("pedidos_confirmados")
        val KEY_ARMADOS = stringPreferencesKey("pedidos_armados")
        val KEY_BACKORDER = stringPreferencesKey("pedidos_backorder")
        val KEY_MERCADOLIBRE = stringPreferencesKey("pedidos_mercadolibre")
        val ES_PRIMERO = stringPreferencesKey("pedidos_es_primero")
        val KEY_COMERCIAL = stringPreferencesKey("pedidos_comercial")
        val KEY_CREDITICIO = stringPreferencesKey("pedidos_crediticio")
        val KEY_CERRADOS = stringPreferencesKey("pedidos_cerrados")
        val KEY_FACTURABLES = stringPreferencesKey("pedidos_facturables")
    }

    init {
        loadPersistedData()
        setupSearchDebounce()
    }

    private fun setupSearchDebounce() {
        viewModelScope.launch {
            _uiState
                .map { it.searchCliente }
                .distinctUntilChanged()
                .debounce(1000)
                .collect { search ->
                    if (search.isNotEmpty()) {
                        searchClientes(search)
                    } else {
                        _uiState.update { it.copy(clientesEncontrados = emptyList()) }
                    }
                }
        }
    }

    fun loadPedidos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getPedidosUseCase(_uiState.value.toPedidosFiltros())) {
                is NetworkResult.Success -> {
                    val pedidos = result.data ?: emptyList()
                    val totalConfirmados = pedidos.count { it.confirmado }
                    val totalBackorder = pedidos.count { it.backOrder == true }
                    val totalMercadoLibre = pedidos.count { it.generadoPorMercadoLibre == true }
                    val montoTotal = pedidos.sumOf { it.total }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pedidos = pedidos,
                            totalPedidos = pedidos.size,
                            totalConfirmados = totalConfirmados,
                            totalBackorder = totalBackorder,
                            totalMercadoLibre = totalMercadoLibre,
                            montoTotal = montoTotal,
                            errorMessage = null
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    private fun searchClientes(search: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingClientes = true) }

            when (val result = clientesRepository.getClientes(search)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingClientes = false,
                            clientesEncontrados = result.data ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingClientes = false) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun setSearchCliente(search: String) {
        _uiState.update { it.copy(searchCliente = search) }
    }

    fun selectCliente(cliente: Cliente) {
        _uiState.update {
            it.copy(
                search = cliente.nombre,
                searchCliente = "",
                clientesEncontrados = emptyList()
            )
        }
        saveFilters()
    }

    fun clearCliente() {
        _uiState.update { it.copy(search = "") }
        saveFilters()
    }

    fun loadVendedores() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingVendedores = true) }

            when (val result = vendedoresRepository.getVendedores()) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingVendedores = false,
                            todosVendedores = result.data ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingVendedores = false) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun setSearchVendedor(search: String) {
        _uiState.update { it.copy(searchVendedor = search) }
    }

    fun selectVendedor(vendedor: Vendedor?) {
        _uiState.update { it.copy(idVendedor = vendedor?.id) }
        saveFilters()
    }

    fun loadRubros() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingRubros = true) }

            when (val result = rubrosRepository.getRubros()) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingRubros = false,
                            todosRubros = result.data ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingRubros = false) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun setSearchRubro(search: String) {
        _uiState.update { it.copy(searchRubro = search) }
    }

    fun toggleRubro(rubro: Rubro) {
        val rubrosIds = _uiState.value.idArticulosRubros.toMutableList()
        if (rubrosIds.contains(rubro.id)) {
            rubrosIds.remove(rubro.id)
        } else {
            rubrosIds.add(rubro.id)
        }
        _uiState.update { it.copy(idArticulosRubros = rubrosIds) }
        saveFilters()
    }

    // Fechas - ahora obligatorias
    fun setFechaDesde(fecha: LocalDate) {
        _uiState.update { it.copy(fechaDesde = fecha) }
        saveFilters()
    }

    fun setFechaHasta(fecha: LocalDate) {
        _uiState.update { it.copy(fechaHasta = fecha) }
        saveFilters()
    }

    fun setSearch(search: String) {
        _uiState.update { it.copy(search = search) }
        saveFilters()
    }

    fun setConfirmados(filtro: FiltroConfirmados) {
        _uiState.update { it.copy(confirmados = filtro) }
        saveFilters()
    }

    fun setArmados(filtro: FiltroArmados) {
        _uiState.update { it.copy(armados = filtro) }
        saveFilters()
    }

    fun setBackorder(filtro: FiltroBackorder) {
        _uiState.update { it.copy(backorder = filtro) }
        saveFilters()
    }

    fun setMercadolibre(filtro: FiltroMercadoLibre) {
        _uiState.update { it.copy(mercadolibre = filtro) }
        saveFilters()
    }

    fun setEsPrimero(filtro: FiltroEsPrimero) {
        _uiState.update { it.copy(esPrimero = filtro) }
        saveFilters()
    }

    fun setComercial(filtro: FiltroAprobacionComercial) {
        _uiState.update { it.copy(comercial = filtro) }
        saveFilters()
    }

    fun setCrediticio(filtro: FiltroAprobacionCrediticia) {
        _uiState.update { it.copy(crediticio = filtro) }
        saveFilters()
    }

    fun setCerrados(filtro: FiltroCerrados) {
        _uiState.update { it.copy(cerrados = filtro) }
        saveFilters()
    }

    fun setFacturables(filtro: FiltroFacturables) {
        _uiState.update { it.copy(facturables = filtro) }
        saveFilters()
    }

    fun resetFiltros() {
        _uiState.update { 
            it.copy(
                search = "",
                idVendedor = null,
                idArticulosRubros = emptyList(),
                confirmados = FiltroConfirmados.TODOS,
                armados = FiltroArmados.TODOS,
                backorder = FiltroBackorder.TODOS,
                mercadolibre = FiltroMercadoLibre.TODOS,
                esPrimero = FiltroEsPrimero.TODOS,
                comercial = FiltroAprobacionComercial.TODOS,
                crediticio = FiltroAprobacionCrediticia.TODOS,
                cerrados = FiltroCerrados.TODOS,
                facturables = FiltroFacturables.TODOS
            )
        }
        saveFilters()
    }

    fun showColumnasSheet() {
        _uiState.update { it.copy(showColumnasSheet = true) }
    }

    fun hideColumnasSheet() {
        _uiState.update { it.copy(showColumnasSheet = false) }
    }

    fun toggleColumna(columna: PedidoColumna) {
        val columnas = _uiState.value.columnasSeleccionadas.toMutableList()
        if (columnas.contains(columna)) {
            if (columnas.size > 1) {
                columnas.remove(columna)
            }
        } else {
            columnas.add(columna)
        }
        _uiState.update { it.copy(columnasSeleccionadas = columnas) }
        saveColumnas()
    }

    fun resetColumnas() {
        _uiState.update { it.copy(columnasSeleccionadas = PedidoColumna.columnasDefault) }
        saveColumnas()
    }

    fun showFiltrosSheet() {
        _uiState.update { it.copy(showFiltrosSheet = true) }
        loadVendedores()
        loadRubros()
    }

    fun hideFiltrosSheet() {
        _uiState.update { it.copy(showFiltrosSheet = false) }
    }

    fun applyFiltros() {
        hideFiltrosSheet()
        loadPedidos()
    }

    private fun saveFilters() {
        if (isLoadingFromStorage) return

        viewModelScope.launch {
            val state = _uiState.value
            dataStore.edit { prefs ->
                // Fechas - ahora obligatorias
                prefs[KEY_FECHA_DESDE] = state.fechaDesde.toEpochDay()
                prefs[KEY_FECHA_HASTA] = state.fechaHasta.toEpochDay()
                
                // Search
                if (state.search.isNotEmpty()) {
                    prefs[KEY_SEARCH] = state.search
                } else {
                    prefs.remove(KEY_SEARCH)
                }
                
                // Vendedor
                state.idVendedor?.let {
                    prefs[KEY_VENDEDOR_ID] = it
                } ?: prefs.remove(KEY_VENDEDOR_ID)
                
                // Rubros
                if (state.idArticulosRubros.isNotEmpty()) {
                    prefs[KEY_RUBROS_IDS] = state.idArticulosRubros.joinToString(",")
                } else {
                    prefs.remove(KEY_RUBROS_IDS)
                }
                
                // Estados booleanos
                prefs[KEY_CONFIRMADOS] = state.confirmados.name
                prefs[KEY_ARMADOS] = state.armados.name
                prefs[KEY_BACKORDER] = state.backorder.name
                prefs[KEY_MERCADOLIBRE] = state.mercadolibre.name
                prefs[ES_PRIMERO] = state.esPrimero.name
                prefs[KEY_COMERCIAL] = state.comercial.name
                prefs[KEY_CREDITICIO] = state.crediticio.name
                prefs[KEY_CERRADOS] = state.cerrados.name
                prefs[KEY_FACTURABLES] = state.facturables.name
            }
        }
    }

    private fun saveColumnas() {
        viewModelScope.launch {
            val columnas = _uiState.value.columnasSeleccionadas
            dataStore.edit { prefs ->
                prefs[KEY_COLUMNAS_SELECCIONADAS] = columnas.joinToString(",") { it.name }
            }
        }
    }

    private fun loadPersistedData() {
        isLoadingFromStorage = true
        viewModelScope.launch {
            dataStore.data.first().let { prefs ->
                // Columnas
                val columnasStr = prefs[KEY_COLUMNAS_SELECCIONADAS]
                val columnas = columnasStr?.split(",")?.mapNotNull {
                    try {
                        PedidoColumna.valueOf(it)
                    } catch (e: Exception) {
                        null
                    }
                }?.takeIf { it.isNotEmpty() } ?: PedidoColumna.columnasDefault

                // Fechas - si no hay guardadas, usar hoy (por defecto en State)
                val fechaDesde = prefs[KEY_FECHA_DESDE]?.let {
                    LocalDate.ofEpochDay(it)
                } ?: LocalDate.now()
                
                val fechaHasta = prefs[KEY_FECHA_HASTA]?.let {
                    LocalDate.ofEpochDay(it)
                } ?: LocalDate.now()

                // Search
                val search = prefs[KEY_SEARCH] ?: ""
                
                // Vendedor
                val vendedorId = prefs[KEY_VENDEDOR_ID]
                
                // Rubros
                val rubrosStr = prefs[KEY_RUBROS_IDS]
                val rubrosIds = rubrosStr?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()

                // Estados booleanos
                val confirmados = prefs[KEY_CONFIRMADOS]?.let {
                    try { FiltroConfirmados.valueOf(it) } catch (e: Exception) { null }
                } ?: FiltroConfirmados.TODOS
                
                val armados = prefs[KEY_ARMADOS]?.let {
                    try { FiltroArmados.valueOf(it) } catch (e: Exception) { null }
                } ?: FiltroArmados.TODOS
                
                val backorder = prefs[KEY_BACKORDER]?.let {
                    try { FiltroBackorder.valueOf(it) } catch (e: Exception) { null }
                } ?: FiltroBackorder.TODOS
                
                val mercadolibre = prefs[KEY_MERCADOLIBRE]?.let {
                    try { FiltroMercadoLibre.valueOf(it) } catch (e: Exception) { null }
                } ?: FiltroMercadoLibre.TODOS
                
                val esPrimero = prefs[ES_PRIMERO]?.let {
                    try { FiltroEsPrimero.valueOf(it) } catch (e: Exception) { null }
                } ?: FiltroEsPrimero.TODOS
                
                val comercial = prefs[KEY_COMERCIAL]?.let {
                    try { FiltroAprobacionComercial.valueOf(it) } catch (e: Exception) { null }
                } ?: FiltroAprobacionComercial.TODOS
                
                val crediticio = prefs[KEY_CREDITICIO]?.let {
                    try { FiltroAprobacionCrediticia.valueOf(it) } catch (e: Exception) { null }
                } ?: FiltroAprobacionCrediticia.TODOS
                
                val cerrados = prefs[KEY_CERRADOS]?.let {
                    try { FiltroCerrados.valueOf(it) } catch (e: Exception) { null }
                } ?: FiltroCerrados.TODOS
                
                val facturables = prefs[KEY_FACTURABLES]?.let {
                    try { FiltroFacturables.valueOf(it) } catch (e: Exception) { null }
                } ?: FiltroFacturables.TODOS

                _uiState.update {
                    it.copy(
                        columnasSeleccionadas = columnas,
                        fechaDesde = fechaDesde,
                        fechaHasta = fechaHasta,
                        search = search,
                        idVendedor = vendedorId,
                        idArticulosRubros = rubrosIds,
                        confirmados = confirmados,
                        armados = armados,
                        backorder = backorder,
                        mercadolibre = mercadolibre,
                        esPrimero = esPrimero,
                        comercial = comercial,
                        crediticio = crediticio,
                        cerrados = cerrados,
                        facturables = facturables
                    )
                }

                isLoadingFromStorage = false
                loadPedidos()
            }
        }
    }
}
