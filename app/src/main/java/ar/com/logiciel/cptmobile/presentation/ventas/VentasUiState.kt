package ar.com.logiciel.cptmobile.presentation.ventas

import ar.com.logiciel.cptmobile.domain.model.*
import java.time.LocalDate

/**
 * Estado de UI para la pantalla de Ventas
 */
data class VentasUiState(
    val isLoading: Boolean = false,
    val ventas: List<Venta> = emptyList(),
    val ventasAgrupadas: List<VentaAgrupada> = emptyList(),
    val resumen: VentasResumen? = null,
    val errorMessage: String? = null,

    // Tipo de agrupación
    val tipoAgrupacion: TipoAgrupacion = TipoAgrupacion.DETALLE,

    // Filtros
    val fechaDesde: LocalDate = LocalDate.now().withDayOfMonth(1), // Primer día del mes actual
    val fechaHasta: LocalDate = LocalDate.now(),
    val textoBusqueda: String = "",
    val clienteSeleccionado: Cliente? = null,
    val articuloSeleccionado: Articulo? = null,
    val rubrosSeleccionados: List<Rubro> = emptyList(),
    val vendedorSeleccionado: Vendedor? = null,
    val proveedorSeleccionado: Proveedor? = null,

    // Estados de búsqueda
    val isLoadingClientes: Boolean = false,
    val clientesEncontrados: List<Cliente> = emptyList(),
    val searchCliente: String = "",

    val isLoadingArticulos: Boolean = false,
    val articulosEncontrados: List<Articulo> = emptyList(),
    val searchArticulo: String = "",

    val isLoadingRubros: Boolean = false,
    val todosRubros: List<Rubro> = emptyList(),
    val searchRubro: String = "",

    val isLoadingVendedores: Boolean = false,
    val todosVendedores: List<Vendedor> = emptyList(),
    val searchVendedor: String = "",

    val isLoadingProveedores: Boolean = false,
    val proveedoresEncontrados: List<Proveedor> = emptyList(),
    val searchProveedor: String = "",

    // Estado del bottom sheet de filtros
    val showFiltrosSheet: Boolean = false
) {
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
}
