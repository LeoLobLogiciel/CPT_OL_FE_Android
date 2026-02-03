package ar.com.logiciel.cptmobile.data.remote.api

import ar.com.logiciel.cptmobile.data.remote.dto.RubrosResponse
import retrofit2.Response
import retrofit2.http.GET

interface RubrosApi {

    /**
     * Obtiene todos los rubros de artículos
     * Endpoint: GET /articulos_rubros
     */
    @GET("articulos_rubros")
    suspend fun getRubros(): Response<RubrosResponse>
}
