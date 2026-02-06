package ar.com.logiciel.cptmobile.domain.usecase.pedidos

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.PedidoPanelCompleto
import ar.com.logiciel.cptmobile.domain.model.PedidosFiltros
import ar.com.logiciel.cptmobile.domain.repository.PedidosRepository
import javax.inject.Inject

class GetPedidosUseCase @Inject constructor(
    private val pedidosRepository: PedidosRepository
) {
    suspend operator fun invoke(filtros: PedidosFiltros): NetworkResult<List<PedidoPanelCompleto>> {
        return pedidosRepository.getPedidos(filtros)
    }
}
