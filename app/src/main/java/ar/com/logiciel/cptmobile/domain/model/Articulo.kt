package ar.com.logiciel.cptmobile.domain.model

/**
 * Modelo de dominio para Artículo
 */
data class Articulo(
    val id: Int,
    val nombre: String,
    val sku: String,
    val stockActual: Int?,
    val stockComprometido: Int?,
    val precioOfertaWEB: Double?,
    val activo: Boolean,
    val marca: MarcaBasic?,
    val articuloRubro: ArticuloRubroBasic?
) {
    /**
     * Stock disponible = stockActual - stockComprometido
     */
    val stockDisponible: Int
        get() = (stockActual ?: 0) - (stockComprometido ?: 0)
}

data class MarcaBasic(
    val id: Int,
    val nombre: String
)

data class ArticuloRubroBasic(
    val id: Int,
    val nombre: String
)
