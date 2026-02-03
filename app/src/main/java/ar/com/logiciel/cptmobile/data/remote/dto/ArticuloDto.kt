package ar.com.logiciel.cptmobile.data.remote.dto

import ar.com.logiciel.cptmobile.domain.model.Articulo
import ar.com.logiciel.cptmobile.domain.model.ArticuloRubroBasic
import ar.com.logiciel.cptmobile.domain.model.MarcaBasic
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticuloDto(
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Nombre")
    val nombre: String,

    @Json(name = "SKU")
    val sku: String,

    @Json(name = "StockActual")
    val stockActual: Int?,

    @Json(name = "StockComprometido")
    val stockComprometido: Int?,

    @Json(name = "PrecioOfertaWEB")
    val precioOfertaWEB: Double?,

    @Json(name = "Activo")
    val activo: Int?,

    @Json(name = "Marca")
    val marca: MarcaBasicDto?,

    @Json(name = "ArticuloRubro")
    val articuloRubro: ArticuloRubroBasicDto?
)

@JsonClass(generateAdapter = true)
data class MarcaBasicDto(
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Nombre")
    val nombre: String
)

@JsonClass(generateAdapter = true)
data class ArticuloRubroBasicDto(
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Nombre")
    val nombre: String
)

@JsonClass(generateAdapter = true)
data class ArticulosResponse(
    @Json(name = "status")
    val status: String,

    @Json(name = "data")
    val data: List<ArticuloDto>
)

fun ArticuloDto.toDomainModel(): Articulo {
    return Articulo(
        id = id,
        nombre = nombre,
        sku = sku,
        stockActual = stockActual,
        stockComprometido = stockComprometido,
        precioOfertaWEB = precioOfertaWEB,
        activo = activo == 1,
        marca = marca?.let { MarcaBasic(it.id, it.nombre) },
        articuloRubro = articuloRubro?.let { ArticuloRubroBasic(it.id, it.nombre) }
    )
}
