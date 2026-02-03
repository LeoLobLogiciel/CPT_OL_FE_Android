package ar.com.logiciel.cptmobile.data.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.data.remote.api.VentasApi
import ar.com.logiciel.cptmobile.data.remote.dto.toDomainModel
import ar.com.logiciel.cptmobile.domain.model.Venta
import ar.com.logiciel.cptmobile.domain.repository.VentasRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VentasRepositoryImpl @Inject constructor(
    private val ventasApi: VentasApi
) : VentasRepository {

    override suspend fun getVentas(
        fechaDesde: String,
        fechaHasta: String,
        search: String?,
        idCliente: Int?,
        idArticulo: Int?,
        idArticulosRubros: List<Int>?,
        idVendedor: Int?,
        idProveedor: Int?
    ): NetworkResult<List<Venta>> {
        return try {
            // Convertir lista de IDs de rubros a string separado por comas
            val rubrosString = idArticulosRubros?.takeIf { it.isNotEmpty() }
                ?.joinToString(",")

            Timber.d("📡 Llamando a API de ventas...")
            Timber.d("Endpoint: GET /facturasClientesDetalle/getAllByMultipleCriteria")
            Timber.d("Parámetros:")
            Timber.d("  - fechaDesde: $fechaDesde")
            Timber.d("  - fechaHasta: $fechaHasta")
            Timber.d("  - search: $search")
            Timber.d("  - idCliente: $idCliente")
            Timber.d("  - idArticulo: $idArticulo")
            Timber.d("  - idArticulosRubros: $rubrosString")
            Timber.d("  - idVendedor: $idVendedor")
            Timber.d("  - idProveedor: $idProveedor")

            val response = ventasApi.getVentas(
                fechaDesde = fechaDesde,
                fechaHasta = fechaHasta,
                search = search,
                idCliente = idCliente,
                idArticulo = idArticulo,
                idArticulosRubros = rubrosString,
                idVendedor = idVendedor,
                idProveedor = idProveedor
            )

            Timber.d("📥 Respuesta recibida - HTTP ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val ventasResponse = response.body()!!

                // Verificar el status de la respuesta
                if (ventasResponse.status == "ERROR") {
                    Timber.e("❌ API retornó ERROR")
                    return NetworkResult.Error("Error al cargar ventas")
                }

                // Manejar el caso donde data puede ser null o estar vacío
                val ventas = ventasResponse.data?.map { it.toDomainModel() } ?: emptyList()

                Timber.d("✅ Response exitoso:")
                Timber.d("   - Status: ${ventasResponse.status}")
                Timber.d("   - Cantidad de ventas: ${ventas.size}")

                NetworkResult.Success(ventas)
            } else {
                val errorMessage = "Error al cargar ventas: ${response.message()}"
                Timber.e("❌ Response con error HTTP ${response.code()}: $errorMessage")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage = "Error de conexión: ${e.localizedMessage}"
            Timber.e("💥 Excepción durante carga de ventas:")
            Timber.e("   Exception type: ${e.javaClass.simpleName}")
            Timber.e("   Message: ${e.message}")
            Timber.e("   Error final: $errorMessage")
            NetworkResult.Error(errorMessage)
        }
    }
}
