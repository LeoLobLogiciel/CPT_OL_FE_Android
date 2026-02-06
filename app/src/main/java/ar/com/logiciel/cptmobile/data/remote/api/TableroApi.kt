package ar.com.logiciel.cptmobile.data.remote.api

import ar.com.logiciel.cptmobile.domain.model.tablero.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TableroApi {

    @GET("tableroPrincipal/ventasWebML")
    suspend fun getVentasWebML(
        @Query("fechaDesde") fechaDesde: String,
        @Query("fechaHasta") fechaHasta: String
    ): Response<VentasWebMLResponse>

    @GET("tableroPrincipal/ventasWebEmpresas")
    suspend fun getVentasWebEmpresas(
        @Query("fechaDesde") fechaDesde: String,
        @Query("fechaHasta") fechaHasta: String
    ): Response<VentasWebEmpresasResponse>

    @GET("tableroPrincipal/venta")
    suspend fun getVentaPorZona(
        @Query("fechaDesde") fechaDesde: String,
        @Query("fechaHasta") fechaHasta: String
    ): Response<VentaPorZonaResponse>

    @GET("tableroPrincipal/deudaClientes")
    suspend fun getDeudaClientes(): Response<DeudaClientesResponse>

    @GET("tableroPrincipal/ventaRubro")
    suspend fun getVentaPorRubro(
        @Query("fechaDesde") fechaDesde: String,
        @Query("fechaHasta") fechaHasta: String
    ): Response<VentaPorRubroResponse>

    @GET("tableroPrincipal/deudaProveedores")
    suspend fun getDeudaProveedores(): Response<DeudaProveedoresResponse>

    @GET("tableroPrincipal/resmas")
    suspend fun getResmas(): Response<ResmasResponse>
}
