package ar.com.logiciel.cptmobile.domain.model.tablero

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VentasWebML(
    @Json(name = "fechaCreacion") val fechaCreacion: String?,
    @Json(name = "cantidad") val cantidadString: String,
    @Json(name = "cantidadFacturadas") val cantidadFacturadasString: String,
    @Json(name = "netoTotal") val netoTotal: String,
    @Json(name = "netoTotalFacturado") val netoTotalFacturado: Double
) {
    val cantidad: Int get() = cantidadString.toIntOrNull() ?: 0
    val cantidadFacturadas: Int get() = cantidadFacturadasString.toIntOrNull() ?: 0
    val netoTotalValue: Double get() = netoTotal.toDoubleOrNull() ?: 0.0
}

@JsonClass(generateAdapter = true)
data class VentasWebMLResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: List<VentasWebML>?
)

@JsonClass(generateAdapter = true)
data class VentasWebEmpresas(
    @Json(name = "cantidad") val cantidadString: String,
    @Json(name = "netoTotal") val netoTotal: Double?
) {
    val cantidad: Int get() = cantidadString.toIntOrNull() ?: 0
}

@JsonClass(generateAdapter = true)
data class VentasWebEmpresasResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: VentasWebEmpresas?
)
