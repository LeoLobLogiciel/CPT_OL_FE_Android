package ar.com.logiciel.cptmobile.data.remote.api

import ar.com.logiciel.cptmobile.data.remote.dto.ArticulosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticulosApi {

    /**
     * Busca artículos por texto
     * Endpoint: GET /articulos/getAllByMultipleCriteria
     */
    @GET("articulos/getAllByMultipleCriteria")
    suspend fun getArticulos(
        @Query("search") search: String
    ): Response<ArticulosResponse>
}
