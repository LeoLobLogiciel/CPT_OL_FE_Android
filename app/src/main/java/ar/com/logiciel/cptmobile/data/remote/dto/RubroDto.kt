package ar.com.logiciel.cptmobile.data.remote.dto

import ar.com.logiciel.cptmobile.domain.model.Rubro
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RubroDto(
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Nombre")
    val nombre: String
)

@JsonClass(generateAdapter = true)
data class RubrosResponse(
    @Json(name = "status")
    val status: String,

    @Json(name = "data")
    val data: List<RubroDto>
)

fun RubroDto.toDomainModel(): Rubro {
    return Rubro(
        id = id,
        nombre = nombre
    )
}
