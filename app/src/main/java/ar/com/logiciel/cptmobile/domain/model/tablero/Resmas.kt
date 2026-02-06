package ar.com.logiciel.cptmobile.domain.model.tablero

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Resma(
    @Json(name = "SKU") val sku: String,
    @Json(name = "Nombre") val nombre: String,
    @Json(name = "StockActual") val stockActualValue: Int?,
    @Json(name = "StockComprometido") val stockComprometidoValue: Int?
) {
    val id: String get() = sku
    val stockActual: Int get() = stockActualValue ?: 0
    val stockComprometido: Int get() = stockComprometidoValue ?: 0
    val stockDisponible: Int get() = stockActual - stockComprometido
}

@JsonClass(generateAdapter = true)
data class ResmasData(
    @Json(name = "resmas") val resmas: List<Resma>?
)

@JsonClass(generateAdapter = true)
data class ResmasResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: ResmasData?
)
