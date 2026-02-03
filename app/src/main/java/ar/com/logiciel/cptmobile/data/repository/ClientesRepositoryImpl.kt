package ar.com.logiciel.cptmobile.data.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.data.remote.api.ClientesApi
import ar.com.logiciel.cptmobile.data.remote.dto.toDomainModel
import ar.com.logiciel.cptmobile.domain.model.Cliente
import ar.com.logiciel.cptmobile.domain.repository.ClientesRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientesRepositoryImpl @Inject constructor(
    private val clientesApi: ClientesApi
) : ClientesRepository {

    override suspend fun getClientes(search: String): NetworkResult<List<Cliente>> {
        return try {
            Timber.d("📡 Llamando a API de clientes...")
            Timber.d("Endpoint: GET /clientes?search=$search")

            val response = clientesApi.getClientes(search)

            Timber.d("📥 Respuesta recibida - HTTP ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val clientesResponse = response.body()!!

                if (clientesResponse.status == "ERROR") {
                    Timber.e("❌ API retornó ERROR")
                    return NetworkResult.Error("Error al buscar clientes")
                }

                val clientes = clientesResponse.data.map { it.toDomainModel() }

                Timber.d("✅ Response exitoso - ${clientes.size} clientes encontrados")

                NetworkResult.Success(clientes)
            } else {
                val errorMessage = "Error al buscar clientes: ${response.message()}"
                Timber.e("❌ Response con error HTTP ${response.code()}: $errorMessage")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage = "Error de conexión: ${e.localizedMessage}"
            Timber.e("💥 Excepción durante búsqueda de clientes: ${e.message}")
            NetworkResult.Error(errorMessage)
        }
    }
}
