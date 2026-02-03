package ar.com.logiciel.cptmobile.data.remote.api

import ar.com.logiciel.cptmobile.data.remote.dto.VentasResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VentasApi {

    /**
     * Obtiene las ventas filtradas por múltiples criterios
     * Endpoint: GET /facturasClientesDetalle/getAllByMultipleCriteria
     */
    @GET("facturasClientesDetalle/getAllByMultipleCriteria")
    suspend fun getVentas(
        @Query("fechaDesde") fechaDesde: String,
        @Query("fechaHasta") fechaHasta: String,
        @Query("search") search: String? = null,
        @Query("idCliente") idCliente: Int? = null,
        @Query("idArticulo") idArticulo: Int? = null,
        @Query("idArticulosRubros") idArticulosRubros: String? = null,
        @Query("idVendedor") idVendedor: Int? = null,
        @Query("idProveedor") idProveedor: Int? = null
    ): Response<VentasResponse>
}
