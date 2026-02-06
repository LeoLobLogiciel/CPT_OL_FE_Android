package ar.com.logiciel.cptmobile.data.remote.api

import ar.com.logiciel.cptmobile.domain.model.PedidoPanelResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PedidosApi {

    @GET("pedidos/getAllByMultipleCriteria")
    suspend fun getPedidos(
        @Query("fechaDesde") fechaDesde: String,
        @Query("fechaHasta") fechaHasta: String,
        @Query("search") search: String? = null,
        @Query("idVendedor") idVendedor: Int? = null,
        @Query("idEstacionArmado") idEstacionArmado: Int? = null,
        @Query("confirmados") confirmados: Boolean? = null,
        @Query("armados") armados: Boolean? = null,
        @Query("backorder") backorder: Boolean? = null,
        @Query("mercadolibre") mercadolibre: Boolean? = null,
        @Query("esPrimero") esPrimero: Boolean? = null,
        @Query("comercial") comercial: Boolean? = null,
        @Query("crediticio") crediticio: Boolean? = null,
        @Query("cerrados") cerrados: Boolean? = null,
        @Query("facturables") facturables: Boolean? = null,
        @Query("idArticulosRubros") idArticulosRubros: String? = null
    ): Response<PedidoPanelResponse>
}
