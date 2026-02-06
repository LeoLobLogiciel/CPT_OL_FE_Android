package ar.com.logiciel.cptmobile.data.repository

import ar.com.logiciel.cptmobile.core.network.NetworkResult
import ar.com.logiciel.cptmobile.data.remote.api.TableroApi
import ar.com.logiciel.cptmobile.domain.model.tablero.*
import ar.com.logiciel.cptmobile.domain.repository.TableroRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TableroRepositoryImpl @Inject constructor(
    private val tableroApi: TableroApi
) : TableroRepository {

    override suspend fun getVentasWebML(fechaDesde: String, fechaHasta: String): NetworkResult<List<VentasWebML>> {
        return try {
            val response = tableroApi.getVentasWebML(fechaDesde, fechaHasta)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.status == "ERROR") {
                    NetworkResult.Error("Error al cargar ventas web ML")
                } else {
                    NetworkResult.Success(body.data ?: emptyList())
                }
            } else {
                NetworkResult.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error cargando ventas web ML")
            NetworkResult.Error("Error de conexión: ${e.localizedMessage}")
        }
    }

    override suspend fun getVentasWebEmpresas(fechaDesde: String, fechaHasta: String): NetworkResult<VentasWebEmpresas?> {
        return try {
            val response = tableroApi.getVentasWebEmpresas(fechaDesde, fechaHasta)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.status == "ERROR") {
                    NetworkResult.Error("Error al cargar ventas web empresas")
                } else {
                    NetworkResult.Success(body.data)
                }
            } else {
                NetworkResult.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error cargando ventas web empresas")
            NetworkResult.Error("Error de conexión: ${e.localizedMessage}")
        }
    }

    override suspend fun getVentaPorZona(fechaDesde: String, fechaHasta: String): NetworkResult<List<VentaPorZona>> {
        return try {
            val response = tableroApi.getVentaPorZona(fechaDesde, fechaHasta)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.status == "ERROR") {
                    NetworkResult.Error("Error al cargar venta por zona")
                } else {
                    NetworkResult.Success(body.data?.venta ?: emptyList())
                }
            } else {
                NetworkResult.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error cargando venta por zona")
            NetworkResult.Error("Error de conexión: ${e.localizedMessage}")
        }
    }

    override suspend fun getDeudaClientes(): NetworkResult<List<DeudaClientesPorZona>> {
        return try {
            val response = tableroApi.getDeudaClientes()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.status == "ERROR") {
                    NetworkResult.Error("Error al cargar deuda clientes")
                } else {
                    NetworkResult.Success(body.data?.venta ?: emptyList())
                }
            } else {
                NetworkResult.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error cargando deuda clientes")
            NetworkResult.Error("Error de conexión: ${e.localizedMessage}")
        }
    }

    override suspend fun getVentaPorRubro(fechaDesde: String, fechaHasta: String): NetworkResult<VentaPorRubroData?> {
        return try {
            val response = tableroApi.getVentaPorRubro(fechaDesde, fechaHasta)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.status == "ERROR") {
                    NetworkResult.Error("Error al cargar venta por rubro")
                } else {
                    NetworkResult.Success(body.data)
                }
            } else {
                NetworkResult.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error cargando venta por rubro")
            NetworkResult.Error("Error de conexión: ${e.localizedMessage}")
        }
    }

    override suspend fun getDeudaProveedores(): NetworkResult<List<DeudaProveedor>> {
        return try {
            val response = tableroApi.getDeudaProveedores()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.status == "ERROR") {
                    NetworkResult.Error("Error al cargar deuda proveedores")
                } else {
                    NetworkResult.Success(body.data?.venta ?: emptyList())
                }
            } else {
                NetworkResult.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error cargando deuda proveedores")
            NetworkResult.Error("Error de conexión: ${e.localizedMessage}")
        }
    }

    override suspend fun getResmas(): NetworkResult<List<Resma>> {
        return try {
            val response = tableroApi.getResmas()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.status == "ERROR") {
                    NetworkResult.Error("Error al cargar resmas")
                } else {
                    NetworkResult.Success(body.data?.resmas ?: emptyList())
                }
            } else {
                NetworkResult.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error cargando resmas")
            NetworkResult.Error("Error de conexión: ${e.localizedMessage}")
        }
    }
}
