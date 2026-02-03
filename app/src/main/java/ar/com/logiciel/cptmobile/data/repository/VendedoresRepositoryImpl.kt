package ar.com.logiciel.cptmobile.data.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.data.remote.api.VendedoresApi
import ar.com.logiciel.cptmobile.data.remote.dto.toDomainModel
import ar.com.logiciel.cptmobile.domain.model.Vendedor
import ar.com.logiciel.cptmobile.domain.repository.VendedoresRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VendedoresRepositoryImpl @Inject constructor(
    private val vendedoresApi: VendedoresApi
) : VendedoresRepository {

    override suspend fun getVendedores(): NetworkResult<List<Vendedor>> {
        return try {
            Timber.d("📡 Llamando a API de vendedores...")
            Timber.d("Endpoint: GET /vendedores")

            val response = vendedoresApi.getVendedores()

            Timber.d("📥 Respuesta recibida - HTTP ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val vendedoresResponse = response.body()!!

                if (vendedoresResponse.status == "ERROR") {
                    Timber.e("❌ API retornó ERROR")
                    return NetworkResult.Error("Error al cargar vendedores")
                }

                val vendedores = vendedoresResponse.data.map { it.toDomainModel() }

                Timber.d("✅ Response exitoso - ${vendedores.size} vendedores cargados")

                NetworkResult.Success(vendedores)
            } else {
                val errorMessage = "Error al cargar vendedores: ${response.message()}"
                Timber.e("❌ Response con error HTTP ${response.code()}: $errorMessage")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage = "Error de conexión: ${e.localizedMessage}"
            Timber.e("💥 Excepción durante carga de vendedores: ${e.message}")
            NetworkResult.Error(errorMessage)
        }
    }
}
