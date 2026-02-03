package ar.com.logiciel.cptmobile.data.remote.dto

import ar.com.logiciel.cptmobile.domain.model.Cliente
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClienteDto(
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Nombre")
    val nombre: String,

    @Json(name = "CUIT")
    val cuit: String?,

    @Json(name = "EmailDocumentacion")
    val email: String?,

    @Json(name = "DomicilioFiscal")
    val domicilioFiscal: String?,

    @Json(name = "CondicionIVA")
    val condicionIVA: String?,

    @Json(name = "SaldoActual")
    val saldoActual: Double?,

    @Json(name = "Activo")
    val activo: Int?
)

@JsonClass(generateAdapter = true)
data class ClientesResponse(
    @Json(name = "status")
    val status: String,

    @Json(name = "data")
    val data: List<ClienteDto>
)

fun ClienteDto.toDomainModel(): Cliente {
    return Cliente(
        id = id,
        nombre = nombre,
        cuit = cuit,
        email = email,
        domicilioFiscal = domicilioFiscal,
        condicionIVA = condicionIVA,
        saldoActual = saldoActual,
        activo = activo == 1
    )
}
