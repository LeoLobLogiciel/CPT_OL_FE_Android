package ar.com.logiciel.cptmobile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "Username")
    val username: String,

    @Json(name = "Password")
    val password: String
)
