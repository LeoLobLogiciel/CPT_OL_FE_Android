package ar.com.logiciel.cptmobile.domain.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.domain.model.Articulo

interface ArticulosRepository {
    suspend fun getArticulos(search: String): NetworkResult<List<Articulo>>
}
