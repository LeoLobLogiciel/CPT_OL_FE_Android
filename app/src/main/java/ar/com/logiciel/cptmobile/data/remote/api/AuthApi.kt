package ar.com.logiciel.cptmobile.data.remote.api

import ar.com.logiciel.cptmobile.data.remote.dto.LoginRequest
import ar.com.logiciel.cptmobile.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}
