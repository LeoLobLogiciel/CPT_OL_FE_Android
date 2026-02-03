package ar.com.logiciel.cptmobile.data.remote.api

import ar.com.logiciel.cptmobile.data.remote.dto.ProveedoresResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProveedoresApi {

    /**
     * Busca proveedores por texto
     * Endpoint: GET /proveedores
     */
    @GET("proveedores")
    suspend fun getProveedores(
        @Query("search") search: String
    ): Response<ProveedoresResponse>
}
