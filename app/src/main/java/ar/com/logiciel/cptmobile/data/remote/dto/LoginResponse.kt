package ar.com.logiciel.cptmobile.data.remote.dto

import ar.com.logiciel.cptmobile.domain.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "status")
    val status: String,

    @Json(name = "data")
    val data: LoginData
)

@JsonClass(generateAdapter = true)
data class LoginData(
    @Json(name = "AccessToken")
    val accessToken: String
)

fun LoginResponse.toDomainModel(): User {
    return User(
        id = 0, // El API no devuelve usuario, solo token
        username = "", // Lo completaremos después si es necesario
        email = null,
        nombre = null,
        apellido = null,
        token = data.accessToken,
        isActive = true
    )
}
