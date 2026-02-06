package ar.com.logiciel.cptmobile.presentation.pedidos

import ar.com.logiciel.cptmobile.domain.model.*
import java.time.LocalDate

/**
 * Estado de UI para la pantalla de Pedidos
 */
data class PedidosUiState(
    val isLoading: Boolean = false,
    val pedidos: List<PedidoPanelCompleto> = emptyList(),
    val errorMessage: String? = null,

    // Columnas seleccionadas (persistidas)
    val columnasSeleccionadas: List<PedidoColumna> = PedidoColumna.columnasDefault,
    val showColumnasSheet: Boolean = false,

    // Filtros - Fechas obligatorias, por defecto hoy
    val fechaDesde: LocalDate = LocalDate.now(),
    val fechaHasta: LocalDate = LocalDate.now(),
    val search: String = "",
    val idVendedor: Int? = null,
    val idArticulosRubros: List<Int> = emptyList(),
    val confirmados: FiltroConfirmados = FiltroConfirmados.TODOS,
    val armados: FiltroArmados = FiltroArmados.TODOS,
    val backorder: FiltroBackorder = FiltroBackorder.TODOS,
    val mercadolibre: FiltroMercadoLibre = FiltroMercadoLibre.TODOS,
    val esPrimero: FiltroEsPrimero = FiltroEsPrimero.TODOS,
    val comercial: FiltroAprobacionComercial = FiltroAprobacionComercial.TODOS,
    val crediticio: FiltroAprobacionCrediticia = FiltroAprobacionCrediticia.TODOS,
    val cerrados: FiltroCerrados = FiltroCerrados.TODOS,
    val facturables: FiltroFacturables = FiltroFacturables.TODOS,
    val showFiltrosSheet: Boolean = false,

    // Estados de búsqueda para filtros
    val searchCliente: String = "",
    val clientesEncontrados: List<Cliente> = emptyList(),
    val isLoadingClientes: Boolean = false,

    val isLoadingVendedores: Boolean = false,
    val todosVendedores: List<Vendedor> = emptyList(),
    val searchVendedor: String = "",

    val isLoadingRubros: Boolean = false,
    val todosRubros: List<Rubro> = emptyList(),
    val searchRubro: String = "",

    // Resumen (para mostrar en la UI)
    val totalPedidos: Int = 0,
    val totalConfirmados: Int = 0,
    val totalBackorder: Int = 0,
    val totalMercadoLibre: Int = 0,
    val montoTotal: Double = 0.0
) {
    /**
     * Vendedores filtrados por búsqueda
     */
    val vendedoresFiltrados: List<Vendedor>
        get() = if (searchVendedor.isEmpty()) {
            todosVendedores
        } else {
            todosVendedores.filter {
                it.nombre.contains(searchVendedor, ignoreCase = true)
            }
        }

    /**
     * Rubros filtrados por búsqueda
     */
    val rubrosFiltrados: List<Rubro>
        get() = if (searchRubro.isEmpty()) {
            todosRubros
        } else {
            todosRubros.filter {
                it.nombre.contains(searchRubro, ignoreCase = true)
            }
        }

    /**
     * Indica si hay filtros activos (excluyendo fechas que son obligatorias)
     */
    val tieneFiltrosActivos: Boolean
        get() = search.isNotBlank() ||
                idVendedor != null ||
                idArticulosRubros.isNotEmpty() ||
                confirmados != FiltroConfirmados.TODOS ||
                armados != FiltroArmados.TODOS ||
                backorder != FiltroBackorder.TODOS ||
                mercadolibre != FiltroMercadoLibre.TODOS ||
                esPrimero != FiltroEsPrimero.TODOS ||
                comercial != FiltroAprobacionComercial.TODOS ||
                crediticio != FiltroAprobacionCrediticia.TODOS ||
                cerrados != FiltroCerrados.TODOS ||
                facturables != FiltroFacturables.TODOS

    /**
     * Crea objeto PedidosFiltros para pasar al use case
     */
    fun toPedidosFiltros(): PedidosFiltros {
        return PedidosFiltros(
            fechaDesde = fechaDesde,
            fechaHasta = fechaHasta,
            search = search,
            idVendedor = idVendedor,
            idArticulosRubros = idArticulosRubros,
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
}
