package ar.com.logiciel.cptmobile.domain.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.PedidoPanelCompleto
import ar.com.logiciel.cptmobile.domain.model.PedidosFiltros

interface PedidosRepository {
    suspend fun getPedidos(filtros: PedidosFiltros): NetworkResult<List<PedidoPanelCompleto>>
}
