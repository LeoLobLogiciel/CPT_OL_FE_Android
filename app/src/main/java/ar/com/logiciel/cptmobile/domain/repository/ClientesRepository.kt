package ar.com.logiciel.cptmobile.domain.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.Cliente

interface ClientesRepository {
    suspend fun getClientes(search: String): NetworkResult<List<Cliente>>
}
