package ar.com.logiciel.cptmobile.data.remote.api

import ar.com.logiciel.cptmobile.data.remote.dto.VendedoresResponse
import retrofit2.Response
import retrofit2.http.GET

interface VendedoresApi {

    /**
     * Obtiene todos los vendedores
     * Endpoint: GET /vendedores
     */
    @GET("vendedores")
    suspend fun getVendedores(): Response<VendedoresResponse>
}
