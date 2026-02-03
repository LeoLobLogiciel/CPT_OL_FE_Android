package ar.com.logiciel.cptmobile.domain.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.Vendedor

interface VendedoresRepository {
    suspend fun getVendedores(): NetworkResult<List<Vendedor>>
}
