package ar.com.logiciel.cptmobile.domain.model.tablero

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VentaPorZona(
    @Json(name = "nombreGrupo") val nombreGrupo: String,
    @Json(name = "netoTotalFacturado") val netoTotalFacturado: Double?,
    @Json(name = "ticketPromedio") val ticketPromedio: Double?,
    @Json(name = "margenLogicielPromedio") val margenLogicielPromedio: String?,
    @Json(name = "cantidadClientesAtendidos") val cantidadClientesAtendidosString: String?,
    @Json(name = "cantidadClientesNuevos") val cantidadClientesNuevosString: String?,
    @Json(name = "porcentajeIncidencia") val porcentajeIncidencia: Double?
) {
    val cantidadClientesAtendidos: Int get() = cantidadClientesAtendidosString?.toIntOrNull() ?: 0
    val cantidadClientesNuevos: Int get() = cantidadClientesNuevosString?.toIntOrNull() ?: 0
    val id: String get() = nombreGrupo
}

@JsonClass(generateAdapter = true)
data class VentaPorZonaData(
    @Json(name = "venta") val venta: List<VentaPorZona>?
)

@JsonClass(generateAdapter = true)
data class VentaPorZonaResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: VentaPorZonaData?
)
