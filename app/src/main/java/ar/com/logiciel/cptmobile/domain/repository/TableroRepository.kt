package ar.com.logiciel.cptmobile.domain.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.tablero.*

interface TableroRepository {
    suspend fun getVentasWebML(fechaDesde: String, fechaHasta: String): NetworkResult<List<VentasWebML>>
    suspend fun getVentasWebEmpresas(fechaDesde: String, fechaHasta: String): NetworkResult<VentasWebEmpresas?>
    suspend fun getVentaPorZona(fechaDesde: String, fechaHasta: String): NetworkResult<List<VentaPorZona>>
    suspend fun getDeudaClientes(): NetworkResult<List<DeudaClientesPorZona>>
    suspend fun getVentaPorRubro(fechaDesde: String, fechaHasta: String): NetworkResult<VentaPorRubroData?>
    suspend fun getDeudaProveedores(): NetworkResult<List<DeudaProveedor>>
    suspend fun getResmas(): NetworkResult<List<Resma>>
}
