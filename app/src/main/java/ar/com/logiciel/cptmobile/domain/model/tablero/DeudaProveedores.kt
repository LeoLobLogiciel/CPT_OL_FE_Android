package ar.com.logiciel.cptmobile.domain.model.tablero

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeudaProveedor(
    @Json(name = "nombre") val nombre: String,
    @Json(name = "saldo") val saldo: Double?,
    @Json(name = "incidencia") val incidencia: Double?
) {
    val id: String get() = nombre
}

@JsonClass(generateAdapter = true)
data class DeudaProveedoresData(
    @Json(name = "venta") val venta: List<DeudaProveedor>?
)

@JsonClass(generateAdapter = true)
data class DeudaProveedoresResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: DeudaProveedoresData?
)
