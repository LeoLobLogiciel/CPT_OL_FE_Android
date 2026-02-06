package ar.com.logiciel.cptmobile.domain.model

import java.time.LocalDate

/**
 * Filtros booleanos para confirmados
 */
enum class FiltroConfirmados(val label: String) {
    TODOS("Todos"),
    SI("Sí"),
    NO("No")
}

/**
 * Filtros booleanos para armados
 */
enum class FiltroArmados(val label: String) {
    TODOS("Todos"),
    SI("Sí"),
    NO("No")
}

/**
 * Filtros booleanos para backorder
 */
enum class FiltroBackorder(val label: String) {
    TODOS("Todos"),
    SI("Sí"),
    NO("No")
}

/**
 * Filtros booleanos para mercadolibre
 */
enum class FiltroMercadoLibre(val label: String) {
    TODOS("Todos"),
    SI("Sí"),
    NO("No")
}

/**
 * Filtros booleanos para esPrimero
 */
enum class FiltroEsPrimero(val label: String) {
    TODOS("Todos"),
    SI("Sí"),
    NO("No")
}

/**
 * Filtros booleanos para aprobación comercial
 */
enum class FiltroAprobacionComercial(val label: String) {
    TODOS("Todos"),
    SI("Sí"),
    NO("No")
}

/**
 * Filtros booleanos para aprobación crediticia
 */
enum class FiltroAprobacionCrediticia(val label: String) {
    TODOS("Todos"),
    SI("Sí"),
    NO("No")
}

/**
 * Filtros booleanos para cerrados
 */
enum class FiltroCerrados(val label: String) {
    TODOS("Todos"),
    SI("Sí"),
    NO("No")
}

/**
 * Filtros booleanos para facturables
 */
enum class FiltroFacturables(val label: String) {
    TODOS("Todos"),
    SI("Sí"),
    NO("No")
}

/**
 * Representación de los filtros para el Panel de Pedidos
 * Fechas son obligatorias - por defecto hoy
 */
data class PedidosFiltros(
    val fechaDesde: LocalDate = LocalDate.now(),
    val fechaHasta: LocalDate = LocalDate.now(),
    val search: String = "",
    val idVendedor: Int? = null,
    val idEstacionArmado: Int? = null,
    val confirmados: FiltroConfirmados = FiltroConfirmados.TODOS,
    val armados: FiltroArmados = FiltroArmados.TODOS,
    val backorder: FiltroBackorder = FiltroBackorder.TODOS,
    val mercadolibre: FiltroMercadoLibre = FiltroMercadoLibre.TODOS,
    val esPrimero: FiltroEsPrimero = FiltroEsPrimero.TODOS,
    val comercial: FiltroAprobacionComercial = FiltroAprobacionComercial.TODOS,
    val crediticio: FiltroAprobacionCrediticia = FiltroAprobacionCrediticia.TODOS,
    val cerrados: FiltroCerrados = FiltroCerrados.TODOS,
    val facturables: FiltroFacturables = FiltroFacturables.TODOS,
    val idArticulosRubros: List<Int> = emptyList()
) {
    /**
     * Verifica si hay algún filtro activo (excluyendo fechas que son obligatorias)
     */
    val tieneFiltrosActivos: Boolean
        get() = search.isNotBlank() ||
                idVendedor != null ||
                idEstacionArmado != null ||
                confirmados != FiltroConfirmados.TODOS ||
                armados != FiltroArmados.TODOS ||
                backorder != FiltroBackorder.TODOS ||
                mercadolibre != FiltroMercadoLibre.TODOS ||
                esPrimero != FiltroEsPrimero.TODOS ||
                comercial != FiltroAprobacionComercial.TODOS ||
                crediticio != FiltroAprobacionCrediticia.TODOS ||
                cerrados != FiltroCerrados.TODOS ||
                facturables != FiltroFacturables.TODOS ||
                idArticulosRubros.isNotEmpty()

    /**
     * Convierte un filtro booleano a valor query param para la API
     */
    fun booleanoAQueryParam(filtro: Any): Boolean? {
        return when (filtro) {
            is FiltroConfirmados -> if (filtro == FiltroConfirmados.TODOS) null else filtro == FiltroConfirmados.SI
            is FiltroArmados -> if (filtro == FiltroArmados.TODOS) null else filtro == FiltroArmados.SI
            is FiltroBackorder -> if (filtro == FiltroBackorder.TODOS) null else filtro == FiltroBackorder.SI
            is FiltroMercadoLibre -> if (filtro == FiltroMercadoLibre.TODOS) null else filtro == FiltroMercadoLibre.SI
            is FiltroEsPrimero -> if (filtro == FiltroEsPrimero.TODOS) null else filtro == FiltroEsPrimero.SI
            is FiltroAprobacionComercial -> if (filtro == FiltroAprobacionComercial.TODOS) null else filtro == FiltroAprobacionComercial.SI
            is FiltroAprobacionCrediticia -> if (filtro == FiltroAprobacionCrediticia.TODOS) null else filtro == FiltroAprobacionCrediticia.SI
            is FiltroCerrados -> if (filtro == FiltroCerrados.TODOS) null else filtro == FiltroCerrados.SI
            is FiltroFacturables -> if (filtro == FiltroFacturables.TODOS) null else filtro == FiltroFacturables.SI
            else -> null
        }
    }
}

/**
 * Información del vendedor para filtro
 */
data class VendedorFiltro(
    val id: Int,
    val nombre: String
)

/**
 * Información de la estación de armado para filtro
 */
data class EstacionArmadoFiltro(
    val id: Int,
    val nombre: String
)

/**
 * Información del rubro para filtro
 */
data class RubroFiltro(
    val id: Int,
    val nombre: String
)
