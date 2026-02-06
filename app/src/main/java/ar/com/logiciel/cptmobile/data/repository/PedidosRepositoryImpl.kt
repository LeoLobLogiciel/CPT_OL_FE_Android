package ar.com.logiciel.cptmobile.data.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.data.remote.api.PedidosApi
import ar.com.logiciel.cptmobile.domain.model.PedidoPanelCompleto
import ar.com.logiciel.cptmobile.domain.model.PedidosFiltros
import ar.com.logiciel.cptmobile.domain.repository.PedidosRepository
import timber.log.Timber
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PedidosRepositoryImpl @Inject constructor(
    private val pedidosApi: PedidosApi
) : PedidosRepository {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override suspend fun getPedidos(filtros: PedidosFiltros): NetworkResult<List<PedidoPanelCompleto>> {
        return try {
            val rubrosString = filtros.idArticulosRubros.takeIf { it.isNotEmpty() }
                ?.joinToString(",")

            Timber.d("📡 Llamando a API de pedidos...")
            Timber.d("Fechas: ${filtros.fechaDesde} - ${filtros.fechaHasta}")

            val response = pedidosApi.getPedidos(
                fechaDesde = filtros.fechaDesde.format(dateFormatter),
                fechaHasta = filtros.fechaHasta.format(dateFormatter),
                search = filtros.search.takeIf { it.isNotBlank() },
                idVendedor = filtros.idVendedor,
                idEstacionArmado = filtros.idEstacionArmado,
                confirmados = filtros.booleanoAQueryParam(filtros.confirmados),
                armados = filtros.booleanoAQueryParam(filtros.armados),
                backorder = filtros.booleanoAQueryParam(filtros.backorder),
                mercadolibre = filtros.booleanoAQueryParam(filtros.mercadolibre),
                esPrimero = filtros.booleanoAQueryParam(filtros.esPrimero),
                comercial = filtros.booleanoAQueryParam(filtros.comercial),
                crediticio = filtros.booleanoAQueryParam(filtros.crediticio),
                cerrados = filtros.booleanoAQueryParam(filtros.cerrados),
                facturables = filtros.booleanoAQueryParam(filtros.facturables),
                idArticulosRubros = rubrosString
            )

            if (response.isSuccessful && response.body() != null) {
                val pedidosResponse = response.body()!!

                if (pedidosResponse.status == "ERROR") {
                    val errorMessage = pedidosResponse.errors.firstOrNull() ?: "Error al cargar pedidos"
                    return NetworkResult.Error(errorMessage)
                }

                val pedidos = pedidosResponse.data ?: emptyList()
                NetworkResult.Success(pedidos)
            } else {
                NetworkResult.Error("Error al cargar pedidos: ${response.message()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error cargando pedidos")
            NetworkResult.Error("Error de conexión: ${e.localizedMessage}")
        }
    }
}
