package ar.com.logiciel.cptmobile.domain.usecase.ventas

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.Venta
import ar.com.logiciel.cptmobile.domain.repository.VentasRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener ventas con filtros
 */
class GetVentasUseCase @Inject constructor(
    private val ventasRepository: VentasRepository
) {
    suspend operator fun invoke(
        fechaDesde: String,
        fechaHasta: String,
        search: String? = null,
        idCliente: Int? = null,
        idArticulo: Int? = null,
        idArticulosRubros: List<Int>? = null,
        idVendedor: Int? = null,
        idProveedor: Int? = null
    ): NetworkResult<List<Venta>> {
        return ventasRepository.getVentas(
            fechaDesde = fechaDesde,
            fechaHasta = fechaHasta,
            search = search,
            idCliente = idCliente,
            idArticulo = idArticulo,
            idArticulosRubros = idArticulosRubros,
            idVendedor = idVendedor,
            idProveedor = idProveedor
        )
    }
}
