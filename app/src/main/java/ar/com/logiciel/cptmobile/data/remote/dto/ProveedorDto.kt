package ar.com.logiciel.cptmobile.data.remote.dto

import ar.com.logiciel.cptmobile.domain.model.Proveedor
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProveedorDto(
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Nombre")
    val nombre: String
)

@JsonClass(generateAdapter = true)
data class ProveedoresResponse(
    @Json(name = "status")
    val status: String,

    @Json(name = "data")
    val data: List<ProveedorDto>
)

fun ProveedorDto.toDomainModel(): Proveedor {
    return Proveedor(
        id = id,
        nombre = nombre
    )
}
