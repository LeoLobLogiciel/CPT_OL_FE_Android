package ar.com.logiciel.cptmobile.data.remote.dto

import ar.com.logiciel.cptmobile.domain.model.Vendedor
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VendedorDto(
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Nombre")
    val nombre: String
)

@JsonClass(generateAdapter = true)
data class VendedoresResponse(
    @Json(name = "status")
    val status: String,

    @Json(name = "data")
    val data: List<VendedorDto>
)

fun VendedorDto.toDomainModel(): Vendedor {
    return Vendedor(
        id = id,
        nombre = nombre
    )
}
