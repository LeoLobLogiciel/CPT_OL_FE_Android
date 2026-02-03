package ar.com.logiciel.cptmobile.domain.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.Proveedor

interface ProveedoresRepository {
    suspend fun getProveedores(search: String): NetworkResult<List<Proveedor>>
}
