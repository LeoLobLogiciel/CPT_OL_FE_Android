package ar.com.logiciel.cptmobile.domain.model.tablero

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeudaClientesPorZona(
    @Json(name = "nombreGrupo") val nombreGrupo: String,
    @Json(name = "saldoActual") val saldoActual: Double,
    @Json(name = "saldoVencido") val saldoVencido: Double,
    @Json(name = "chequesVigentes") val chequesVigentes: Double,
    @Json(name = "promedioPagos") val promedioPagos: Double,
    @Json(name = "porcentajeVencido") val porcentajeVencido: Double?
) {
    val id: String get() = nombreGrupo
}

@JsonClass(generateAdapter = true)
data class DeudaClientesData(
    @Json(name = "venta") val venta: List<DeudaClientesPorZona>?
)

@JsonClass(generateAdapter = true)
data class DeudaClientesResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: DeudaClientesData?
)
