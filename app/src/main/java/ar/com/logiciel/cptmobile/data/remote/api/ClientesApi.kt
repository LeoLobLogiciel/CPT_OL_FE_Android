package ar.com.logiciel.cptmobile.data.remote.api

import ar.com.logiciel.cptmobile.data.remote.dto.ClientesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ClientesApi {

    /**
     * Busca clientes por texto
     * Endpoint: GET /clientes
     */
    @GET("clientes")
    suspend fun getClientes(
        @Query("search") search: String
    ): Response<ClientesResponse>
}
