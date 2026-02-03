package ar.com.logiciel.cptmobile.data.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.data.remote.api.ArticulosApi
import ar.com.logiciel.cptmobile.data.remote.dto.toDomainModel
import ar.com.logiciel.cptmobile.domain.model.Articulo
import ar.com.logiciel.cptmobile.domain.repository.ArticulosRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticulosRepositoryImpl @Inject constructor(
    private val articulosApi: ArticulosApi
) : ArticulosRepository {

    override suspend fun getArticulos(search: String): NetworkResult<List<Articulo>> {
        return try {
            Timber.d("📡 Llamando a API de artículos...")
            Timber.d("Endpoint: GET /articulos?search=$search")

            val response = articulosApi.getArticulos(search)

            Timber.d("📥 Respuesta recibida - HTTP ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val articulosResponse = response.body()!!

                if (articulosResponse.status == "ERROR") {
                    Timber.e("❌ API retornó ERROR")
                    return NetworkResult.Error("Error al buscar artículos")
                }

                val articulos = articulosResponse.data.map { it.toDomainModel() }

                Timber.d("✅ Response exitoso - ${articulos.size} artículos encontrados")

                NetworkResult.Success(articulos)
            } else {
                val errorMessage = "Error al buscar artículos: ${response.message()}"
                Timber.e("❌ Response con error HTTP ${response.code()}: $errorMessage")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage = "Error de conexión: ${e.localizedMessage}"
            Timber.e("💥 Excepción durante búsqueda de artículos: ${e.message}")
            NetworkResult.Error(errorMessage)
        }
    }
}
