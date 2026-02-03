package ar.com.logiciel.cptmobile.data.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.data.remote.api.ProveedoresApi
import ar.com.logiciel.cptmobile.data.remote.dto.toDomainModel
import ar.com.logiciel.cptmobile.domain.model.Proveedor
import ar.com.logiciel.cptmobile.domain.repository.ProveedoresRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProveedoresRepositoryImpl @Inject constructor(
    private val proveedoresApi: ProveedoresApi
) : ProveedoresRepository {

    override suspend fun getProveedores(search: String): NetworkResult<List<Proveedor>> {
        return try {
            Timber.d("📡 Llamando a API de proveedores...")
            Timber.d("Endpoint: GET /proveedores?search=$search")

            val response = proveedoresApi.getProveedores(search)

            Timber.d("📥 Respuesta recibida - HTTP ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val proveedoresResponse = response.body()!!

                if (proveedoresResponse.status == "ERROR") {
                    Timber.e("❌ API retornó ERROR")
                    return NetworkResult.Error("Error al buscar proveedores")
                }

                val proveedores = proveedoresResponse.data.map { it.toDomainModel() }

                Timber.d("✅ Response exitoso - ${proveedores.size} proveedores encontrados")

                NetworkResult.Success(proveedores)
            } else {
                val errorMessage = "Error al buscar proveedores: ${response.message()}"
                Timber.e("❌ Response con error HTTP ${response.code()}: $errorMessage")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage = "Error de conexión: ${e.localizedMessage}"
            Timber.e("💥 Excepción durante búsqueda de proveedores: ${e.message}")
            NetworkResult.Error(errorMessage)
        }
    }
}
