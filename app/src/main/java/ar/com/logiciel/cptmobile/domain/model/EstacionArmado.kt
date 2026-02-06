package ar.com.logiciel.cptmobile.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EstacionArmado(
    @Json(name = "Id") val id: Int,
    @Json(name = "Nombre") val nombre: String
)

@JsonClass(generateAdapter = true)
data class EstacionesArmadoResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: List<EstacionArmado>
)
