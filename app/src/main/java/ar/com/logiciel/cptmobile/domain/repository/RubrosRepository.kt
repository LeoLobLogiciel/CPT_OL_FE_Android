package ar.com.logiciel.cptmobile.domain.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.Rubro

interface RubrosRepository {
    suspend fun getRubros(): NetworkResult<List<Rubro>>
}
