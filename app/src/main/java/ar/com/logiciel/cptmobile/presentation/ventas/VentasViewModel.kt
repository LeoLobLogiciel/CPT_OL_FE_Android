package ar.com.logiciel.cptmobile.presentation.ventas

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
import ar.com.logiciel.cptmobile.domain.repository.*
import ar.com.logiciel.cptmobile.domain.usecase.ventas.AgruparVentasUseCase
import ar.com.logiciel.cptmobile.domain.usecase.ventas.CalcularResumenUseCase
import ar.com.logiciel.cptmobile.domain.usecase.ventas.GetVentasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class VentasViewModel @Inject constructor(
    private val getVentasUseCase: GetVentasUseCase,
    private val agruparVentasUseCase: AgruparVentasUseCase,
    private val calcularResumenUseCase: CalcularResumenUseCase,
    private val clientesRepository: ClientesRepository,
    private val articulosRepository: ArticulosRepository,
    private val rubrosRepository: RubrosRepository,
    private val vendedoresRepository: VendedoresRepository,
    private val proveedoresRepository: ProveedoresRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(VentasUiState())
    val uiState: StateFlow<VentasUiState> = _uiState.asStateFlow()

    private var isLoadingFilters = false
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Keys para DataStore
    private companion object {
        val KEY_TIPO_AGRUPACION = stringPreferencesKey("ventas_tipo_agrupacion")
        val KEY_FECHA_DESDE = longPreferencesKey("ventas_fecha_desde")
        val KEY_FECHA_HASTA = longPreferencesKey("ventas_fecha_hasta")
        val KEY_TEXTO_BUSQUEDA = stringPreferencesKey("ventas_texto_busqueda")
        val KEY_CLIENTE_ID = intPreferencesKey("ventas_cliente_id")
        val KEY_CLIENTE_NOMBRE = stringPreferencesKey("ventas_cliente_nombre")
        val KEY_ARTICULO_ID = intPreferencesKey("ventas_articulo_id")
        val KEY_ARTICULO_NOMBRE = stringPreferencesKey("ventas_articulo_nombre")
        val KEY_ARTICULO_SKU = stringPreferencesKey("ventas_articulo_sku")
        val KEY_VENDEDOR_ID = intPreferencesKey("ventas_vendedor_id")
        val KEY_VENDEDOR_NOMBRE = stringPreferencesKey("ventas_vendedor_nombre")
        val KEY_PROVEEDOR_ID = intPreferencesKey("ventas_proveedor_id")
        val KEY_PROVEEDOR_NOMBRE = stringPreferencesKey("ventas_proveedor_nombre")
    }

    init {
        Timber.d("🛒 VentasViewModel INICIALIZADO")
        loadFilters()
        setupSearchDebounce()
        Timber.d("🛒 VentasViewModel - Estado inicial: ${_uiState.value}")
    }

    /**
     * Configurar debounce para búsquedas
     */
    private fun setupSearchDebounce() {
        // Debounce para búsqueda de clientes (1 segundo)
        viewModelScope.launch {
            _uiState
                .map { it.searchCliente }
                .distinctUntilChanged()
                .debounce(1000)
                .collect { search ->
                    if (search.isNotEmpty() && _uiState.value.clienteSeleccionado == null) {
                        searchClientes(search)
                    } else if (search.isEmpty()) {
                        _uiState.update { it.copy(clientesEncontrados = emptyList()) }
                    }
                }
        }

        // Debounce para búsqueda de artículos (1 segundo)
        viewModelScope.launch {
            _uiState
                .map { it.searchArticulo }
                .distinctUntilChanged()
                .debounce(1000)
                .collect { search ->
                    if (search.isNotEmpty() && _uiState.value.articuloSeleccionado == null) {
                        searchArticulos(search)
                    } else if (search.isEmpty()) {
                        _uiState.update { it.copy(articulosEncontrados = emptyList()) }
                    }
                }
        }

        // Debounce para búsqueda de proveedores (1 segundo)
        viewModelScope.launch {
            _uiState
                .map { it.searchProveedor }
                .distinctUntilChanged()
                .debounce(1000)
                .collect { search ->
                    if (search.isNotEmpty() && _uiState.value.proveedorSeleccionado == null) {
                        searchProveedores(search)
                    } else if (search.isEmpty()) {
                        _uiState.update { it.copy(proveedoresEncontrados = emptyList()) }
                    }
                }
        }
    }

    /**
     * Cargar ventas con filtros actuales
     */
    fun loadVentas() {
        viewModelScope.launch {
            Timber.d("🔄 Cargando ventas con filtros aplicados")
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val state = _uiState.value
            val result = getVentasUseCase(
                fechaDesde = state.fechaDesde.format(dateFormatter),
                fechaHasta = state.fechaHasta.format(dateFormatter),
                search = state.textoBusqueda.takeIf { it.isNotEmpty() },
                idCliente = state.clienteSeleccionado?.id,
                idArticulo = state.articuloSeleccionado?.id,
                idArticulosRubros = state.rubrosSeleccionados.takeIf { it.isNotEmpty() }?.map { it.id },
                idVendedor = state.vendedorSeleccionado?.id,
                idProveedor = state.proveedorSeleccionado?.id
            )

            when (result) {
                is NetworkResult.Success -> {
                    val ventas = result.data ?: emptyList()
                    val resumen = calcularResumenUseCase(ventas)
                    val ventasAgrupadas = agruparVentasUseCase(ventas, state.tipoAgrupacion)

                    Timber.d("✅ Ventas cargadas: ${ventas.size} registros")

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            ventas = ventas,
                            resumen = resumen,
                            ventasAgrupadas = ventasAgrupadas,
                            errorMessage = null
                        )
                    }
                }
                is NetworkResult.Error -> {
                    Timber.e("❌ Error cargando ventas: ${result.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    // Ya está en loading
                }
            }
        }
    }

    /**
     * Cambiar tipo de agrupación
     */
    fun setTipoAgrupacion(tipo: TipoAgrupacion) {
        Timber.d("🔄 Cambiando tipo de agrupación a: $tipo")
        _uiState.update { it.copy(tipoAgrupacion = tipo) }

        // Reagrupar con el nuevo tipo
        val ventas = _uiState.value.ventas
        val ventasAgrupadas = agruparVentasUseCase(ventas, tipo)
        _uiState.update { it.copy(ventasAgrupadas = ventasAgrupadas) }

        saveFilters()
    }

    /**
     * Actualizar fecha desde
     */
    fun setFechaDesde(fecha: LocalDate) {
        _uiState.update { it.copy(fechaDesde = fecha) }
        saveFilters()
    }

    /**
     * Actualizar fecha hasta
     */
    fun setFechaHasta(fecha: LocalDate) {
        _uiState.update { it.copy(fechaHasta = fecha) }
        saveFilters()
    }

    /**
     * Actualizar texto de búsqueda
     */
    fun setTextoBusqueda(texto: String) {
        _uiState.update { it.copy(textoBusqueda = texto) }
        saveFilters()
    }

    /**
     * Actualizar búsqueda de cliente
     */
    fun setSearchCliente(search: String) {
        _uiState.update { it.copy(searchCliente = search) }
    }

    /**
     * Buscar clientes
     */
    private fun searchClientes(search: String) {
        viewModelScope.launch {
            Timber.d("🔍 Buscando clientes: $search")
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
                    Timber.e("❌ Error buscando clientes: ${result.message}")
                    _uiState.update { it.copy(isLoadingClientes = false) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /**
     * Seleccionar cliente
     */
    fun selectCliente(cliente: Cliente) {
        _uiState.update {
            it.copy(
                clienteSeleccionado = cliente,
                searchCliente = "",
                clientesEncontrados = emptyList()
            )
        }
        saveFilters()
    }

    /**
     * Quitar cliente seleccionado
     */
    fun clearCliente() {
        _uiState.update { it.copy(clienteSeleccionado = null) }
        saveFilters()
    }

    /**
     * Actualizar búsqueda de artículo
     */
    fun setSearchArticulo(search: String) {
        _uiState.update { it.copy(searchArticulo = search) }
    }

    /**
     * Buscar artículos
     */
    private fun searchArticulos(search: String) {
        viewModelScope.launch {
            Timber.d("🔍 Buscando artículos: $search")
            _uiState.update { it.copy(isLoadingArticulos = true) }

            when (val result = articulosRepository.getArticulos(search)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingArticulos = false,
                            articulosEncontrados = result.data ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error -> {
                    Timber.e("❌ Error buscando artículos: ${result.message}")
                    _uiState.update { it.copy(isLoadingArticulos = false) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /**
     * Seleccionar artículo
     */
    fun selectArticulo(articulo: Articulo) {
        _uiState.update {
            it.copy(
                articuloSeleccionado = articulo,
                searchArticulo = "",
                articulosEncontrados = emptyList()
            )
        }
        saveFilters()
    }

    /**
     * Quitar artículo seleccionado
     */
    fun clearArticulo() {
        _uiState.update { it.copy(articuloSeleccionado = null) }
        saveFilters()
    }

    /**
     * Cargar todos los rubros (cuando se abre filtros)
     */
    fun loadRubros() {
        viewModelScope.launch {
            Timber.d("🔄 Cargando rubros...")
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
                    Timber.e("❌ Error cargando rubros: ${result.message}")
                    _uiState.update { it.copy(isLoadingRubros = false) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /**
     * Actualizar búsqueda de rubro
     */
    fun setSearchRubro(search: String) {
        _uiState.update { it.copy(searchRubro = search) }
    }

    /**
     * Toggle rubro en selección múltiple
     */
    fun toggleRubro(rubro: Rubro) {
        val rubros = _uiState.value.rubrosSeleccionados.toMutableList()
        if (rubros.any { it.id == rubro.id }) {
            rubros.removeAll { it.id == rubro.id }
        } else {
            rubros.add(rubro)
        }
        _uiState.update { it.copy(rubrosSeleccionados = rubros) }
        saveFilters()
    }

    /**
     * Cargar todos los vendedores (cuando se abre filtros)
     */
    fun loadVendedores() {
        viewModelScope.launch {
            Timber.d("🔄 Cargando vendedores...")
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
                    Timber.e("❌ Error cargando vendedores: ${result.message}")
                    _uiState.update { it.copy(isLoadingVendedores = false) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /**
     * Actualizar búsqueda de vendedor
     */
    fun setSearchVendedor(search: String) {
        _uiState.update { it.copy(searchVendedor = search) }
    }

    /**
     * Seleccionar vendedor (radio button - selección única)
     */
    fun selectVendedor(vendedor: Vendedor) {
        _uiState.update { it.copy(vendedorSeleccionado = vendedor) }
        saveFilters()
    }

    /**
     * Quitar vendedor seleccionado
     */
    fun clearVendedor() {
        _uiState.update { it.copy(vendedorSeleccionado = null) }
        saveFilters()
    }

    /**
     * Actualizar búsqueda de proveedor
     */
    fun setSearchProveedor(search: String) {
        _uiState.update { it.copy(searchProveedor = search) }
    }

    /**
     * Buscar proveedores
     */
    private fun searchProveedores(search: String) {
        viewModelScope.launch {
            Timber.d("🔍 Buscando proveedores: $search")
            _uiState.update { it.copy(isLoadingProveedores = true) }

            when (val result = proveedoresRepository.getProveedores(search)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingProveedores = false,
                            proveedoresEncontrados = result.data ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error -> {
                    Timber.e("❌ Error buscando proveedores: ${result.message}")
                    _uiState.update { it.copy(isLoadingProveedores = false) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /**
     * Seleccionar proveedor
     */
    fun selectProveedor(proveedor: Proveedor) {
        _uiState.update {
            it.copy(
                proveedorSeleccionado = proveedor,
                searchProveedor = "",
                proveedoresEncontrados = emptyList()
            )
        }
        saveFilters()
    }

    /**
     * Quitar proveedor seleccionado
     */
    fun clearProveedor() {
        _uiState.update { it.copy(proveedorSeleccionado = null) }
        saveFilters()
    }

    /**
     * Mostrar bottom sheet de filtros
     */
    fun showFiltros() {
        _uiState.update {
            it.copy(
                showFiltrosSheet = true,
                searchCliente = "",
                searchArticulo = "",
                searchRubro = "",
                searchVendedor = "",
                searchProveedor = "",
                clientesEncontrados = emptyList(),
                articulosEncontrados = emptyList(),
                proveedoresEncontrados = emptyList()
            )
        }
        // Cargar rubros y vendedores al abrir filtros
        loadRubros()
        loadVendedores()
    }

    /**
     * Cerrar bottom sheet de filtros
     */
    fun hideFiltros() {
        _uiState.update { it.copy(showFiltrosSheet = false) }
    }

    /**
     * Aplicar filtros y recargar ventas
     */
    fun applyFiltros() {
        hideFiltros()
        loadVentas()
    }

    /**
     * Guardar filtros en DataStore
     */
    private fun saveFilters() {
        if (isLoadingFilters) return

        viewModelScope.launch {
            val state = _uiState.value
            dataStore.edit { preferences ->
                preferences[KEY_TIPO_AGRUPACION] = state.tipoAgrupacion.name
                preferences[KEY_FECHA_DESDE] = state.fechaDesde.toEpochDay()
                preferences[KEY_FECHA_HASTA] = state.fechaHasta.toEpochDay()
                preferences[KEY_TEXTO_BUSQUEDA] = state.textoBusqueda

                // Cliente
                state.clienteSeleccionado?.let {
                    preferences[KEY_CLIENTE_ID] = it.id
                    preferences[KEY_CLIENTE_NOMBRE] = it.nombre
                } ?: run {
                    preferences.remove(KEY_CLIENTE_ID)
                    preferences.remove(KEY_CLIENTE_NOMBRE)
                }

                // Artículo
                state.articuloSeleccionado?.let {
                    preferences[KEY_ARTICULO_ID] = it.id
                    preferences[KEY_ARTICULO_NOMBRE] = it.nombre
                    preferences[KEY_ARTICULO_SKU] = it.sku
                } ?: run {
                    preferences.remove(KEY_ARTICULO_ID)
                    preferences.remove(KEY_ARTICULO_NOMBRE)
                    preferences.remove(KEY_ARTICULO_SKU)
                }

                // Vendedor
                state.vendedorSeleccionado?.let {
                    preferences[KEY_VENDEDOR_ID] = it.id
                    preferences[KEY_VENDEDOR_NOMBRE] = it.nombre
                } ?: run {
                    preferences.remove(KEY_VENDEDOR_ID)
                    preferences.remove(KEY_VENDEDOR_NOMBRE)
                }

                // Proveedor
                state.proveedorSeleccionado?.let {
                    preferences[KEY_PROVEEDOR_ID] = it.id
                    preferences[KEY_PROVEEDOR_NOMBRE] = it.nombre
                } ?: run {
                    preferences.remove(KEY_PROVEEDOR_ID)
                    preferences.remove(KEY_PROVEEDOR_NOMBRE)
                }
            }
            Timber.d("💾 Filtros guardados en DataStore")
        }
    }

    /**
     * Cargar filtros desde DataStore
     */
    private fun loadFilters() {
        isLoadingFilters = true
        viewModelScope.launch {
            dataStore.data.first().let { preferences ->
                val tipoAgrupacion = preferences[KEY_TIPO_AGRUPACION]?.let {
                    try {
                        TipoAgrupacion.valueOf(it)
                    } catch (e: Exception) {
                        TipoAgrupacion.DETALLE
                    }
                } ?: TipoAgrupacion.DETALLE

                val fechaDesde = preferences[KEY_FECHA_DESDE]?.let {
                    LocalDate.ofEpochDay(it)
                } ?: LocalDate.now().withDayOfMonth(1)

                val fechaHasta = preferences[KEY_FECHA_HASTA]?.let {
                    LocalDate.ofEpochDay(it)
                } ?: LocalDate.now()

                val textoBusqueda = preferences[KEY_TEXTO_BUSQUEDA] ?: ""

                val cliente = preferences[KEY_CLIENTE_ID]?.let { id ->
                    preferences[KEY_CLIENTE_NOMBRE]?.let { nombre ->
                        Cliente(
                            id = id,
                            nombre = nombre,
                            cuit = null,
                            email = null,
                            domicilioFiscal = null,
                            condicionIVA = null,
                            saldoActual = null,
                            activo = true
                        )
                    }
                }

                val articulo = preferences[KEY_ARTICULO_ID]?.let { id ->
                    preferences[KEY_ARTICULO_NOMBRE]?.let { nombre ->
                        preferences[KEY_ARTICULO_SKU]?.let { sku ->
                            Articulo(
                                id = id,
                                nombre = nombre,
                                sku = sku,
                                stockActual = null,
                                stockComprometido = null,
                                precioOfertaWEB = null,
                                activo = true,
                                marca = null,
                                articuloRubro = null
                            )
                        }
                    }
                }

                val vendedor = preferences[KEY_VENDEDOR_ID]?.let { id ->
                    preferences[KEY_VENDEDOR_NOMBRE]?.let { nombre ->
                        Vendedor(id = id, nombre = nombre)
                    }
                }

                val proveedor = preferences[KEY_PROVEEDOR_ID]?.let { id ->
                    preferences[KEY_PROVEEDOR_NOMBRE]?.let { nombre ->
                        Proveedor(id = id, nombre = nombre)
                    }
                }

                _uiState.update {
                    it.copy(
                        tipoAgrupacion = tipoAgrupacion,
                        fechaDesde = fechaDesde,
                        fechaHasta = fechaHasta,
                        textoBusqueda = textoBusqueda,
                        clienteSeleccionado = cliente,
                        articuloSeleccionado = articulo,
                        vendedorSeleccionado = vendedor,
                        proveedorSeleccionado = proveedor
                    )
                }

                Timber.d("📥 Filtros cargados desde DataStore")
                isLoadingFilters = false

                // Cargar ventas con los filtros restaurados
                loadVentas()
            }
        }
    }
}
