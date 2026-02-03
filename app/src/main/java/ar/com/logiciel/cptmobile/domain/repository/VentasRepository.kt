package ar.com.logiciel.cptmobile.domain.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.Venta

interface VentasRepository {
    suspend fun getVentas(
        fechaDesde: String,
        fechaHasta: String,
        search: String? = null,
        idCliente: Int? = null,
        idArticulo: Int? = null,
        idArticulosRubros: List<Int>? = null,
        idVendedor: Int? = null,
        idProveedor: Int? = null
    ): NetworkResult<List<Venta>>
}
