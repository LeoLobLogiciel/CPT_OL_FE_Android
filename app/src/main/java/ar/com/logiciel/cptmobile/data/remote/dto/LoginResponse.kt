package ar.com.logiciel.cptmobile.data.remote.dto

import ar.com.logiciel.cptmobile.domain.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "AccessToken")
    val accessToken: String,

    @Json(name = "Usuario")
    val usuario: UsuarioDto? = null
)

@JsonClass(generateAdapter = true)
data class UsuarioDto(
    @Json(name = "Id")
    val id: Int,

    @Json(name = "Username")
    val username: String,

    @Json(name = "Email")
    val email: String? = null,

    @Json(name = "Nombre")
    val nombre: String? = null,

    @Json(name = "Apellido")
    val apellido: String? = null,

    @Json(name = "Activo")
    val activo: Boolean = true
)

fun LoginResponse.toDomainModel(): User {
    return User(
        id = usuario?.id ?: 0,
        username = usuario?.username ?: "",
        email = usuario?.email,
        nombre = usuario?.nombre,
        apellido = usuario?.apellido,
        token = accessToken,
        isActive = usuario?.activo ?: true
    )
}
