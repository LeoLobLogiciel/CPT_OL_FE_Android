package ar.com.logiciel.cptmobile.data.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.data.remote.api.RubrosApi
import ar.com.logiciel.cptmobile.data.remote.dto.toDomainModel
import ar.com.logiciel.cptmobile.domain.model.Rubro
import ar.com.logiciel.cptmobile.domain.repository.RubrosRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RubrosRepositoryImpl @Inject constructor(
    private val rubrosApi: RubrosApi
) : RubrosRepository {

    override suspend fun getRubros(): NetworkResult<List<Rubro>> {
        return try {
            Timber.d("📡 Llamando a API de rubros...")
            Timber.d("Endpoint: GET /articulos_rubros")

            val response = rubrosApi.getRubros()

            Timber.d("📥 Respuesta recibida - HTTP ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val rubrosResponse = response.body()!!

                if (rubrosResponse.status == "ERROR") {
                    Timber.e("❌ API retornó ERROR")
                    return NetworkResult.Error("Error al cargar rubros")
                }

                val rubros = rubrosResponse.data.map { it.toDomainModel() }

                Timber.d("✅ Response exitoso - ${rubros.size} rubros cargados")

                NetworkResult.Success(rubros)
            } else {
                val errorMessage = "Error al cargar rubros: ${response.message()}"
                Timber.e("❌ Response con error HTTP ${response.code()}: $errorMessage")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage = "Error de conexión: ${e.localizedMessage}"
            Timber.e("💥 Excepción durante carga de rubros: ${e.message}")
            NetworkResult.Error(errorMessage)
        }
    }
}
