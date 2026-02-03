package ar.com.logiciel.cptmobile.domain.usecase.ventas

import ar.com.logiciel.cptmobile.domain.model.TipoAgrupacion
import ar.com.logiciel.cptmobile.domain.model.Venta
import ar.com.logiciel.cptmobile.domain.model.VentaAgrupada
import timber.log.Timber
import javax.inject.Inject

/**
 * Caso de uso para agrupar ventas según diferentes criterios
 * Replica exactamente la lógica del ViewModel de iOS
 */
class AgruparVentasUseCase @Inject constructor() {

    operator fun invoke(ventas: List<Venta>, tipoAgrupacion: TipoAgrupacion): List<VentaAgrupada> {
        if (tipoAgrupacion == TipoAgrupacion.DETALLE) {
            return emptyList()
        }

        Timber.d("🔄 Agrupando ${ventas.size} ventas por $tipoAgrupacion")

        val agrupadas = when (tipoAgrupacion) {
            TipoAgrupacion.POR_CLIENTE -> agruparPorCliente(ventas)
            TipoAgrupacion.POR_ARTICULO -> agruparPorArticulo(ventas)
            TipoAgrupacion.POR_RUBRO -> agruparPorRubro(ventas)
            TipoAgrupacion.POR_VENDEDOR -> agruparPorVendedor(ventas)
            TipoAgrupacion.POR_PROVEEDOR -> agruparPorProveedor(ventas)
            TipoAgrupacion.POR_FECHA -> agruparPorFecha(ventas)
            else -> emptyList()
        }

        // Calcular incidencia
        val total = agrupadas.sumOf { it.importeTotal }
        val agrupadasConIncidencia = agrupadas.map { ventaAgrupada ->
            ventaAgrupada.copy(
                incidencia = if (total > 0) (ventaAgrupada.importeTotal / total) * 100 else 0.0
            )
        }

        Timber.d("✅ Agrupación completa: ${agrupadasConIncidencia.size} grupos")

        return agrupadasConIncidencia
    }

    private fun agruparPorCliente(ventas: List<Venta>): List<VentaAgrupada> {
        val dict = mutableMapOf<String, Triple<Double, Double, Int>>()

        for (venta in ventas) {
            val key = venta.facturaClienteClienteNombre
            val current = dict[key] ?: Triple(0.0, 0.0, 0)
            dict[key] = Triple(
                current.first + (venta.cantidad * venta.precioUnitario),
                current.second + venta.cantidad,
                current.third + 1
            )
        }

        return dict.map { (nombre, triple) ->
            VentaAgrupada(
                nombre = nombre,
                importeTotal = triple.first,
                cantidadUnidades = triple.second,
                cantidadVentas = triple.third
            )
        }.sortedByDescending { it.importeTotal }
    }

    private fun agruparPorArticulo(ventas: List<Venta>): List<VentaAgrupada> {
        val dict = mutableMapOf<String, Triple<Double, Double, Int>>()

        for (venta in ventas) {
            val key = venta.articuloNombre ?: "Sin artículo"
            val current = dict[key] ?: Triple(0.0, 0.0, 0)
            dict[key] = Triple(
                current.first + (venta.cantidad * venta.precioUnitario),
                current.second + venta.cantidad,
                current.third + 1
            )
        }

        return dict.map { (nombre, triple) ->
            VentaAgrupada(
                nombre = nombre,
                importeTotal = triple.first,
                cantidadUnidades = triple.second,
                cantidadVentas = triple.third
            )
        }.sortedByDescending { it.importeTotal }
    }

    private fun agruparPorRubro(ventas: List<Venta>): List<VentaAgrupada> {
        val dict = mutableMapOf<String, Triple<Double, Double, Int>>()

        for (venta in ventas) {
            val key = venta.articuloArticuloRubroNombre ?: "Sin rubro"
            val current = dict[key] ?: Triple(0.0, 0.0, 0)
            dict[key] = Triple(
                current.first + (venta.cantidad * venta.precioUnitario),
                current.second + venta.cantidad,
                current.third + 1
            )
        }

        return dict.map { (nombre, triple) ->
            VentaAgrupada(
                nombre = nombre,
                importeTotal = triple.first,
                cantidadUnidades = triple.second,
                cantidadVentas = triple.third
            )
        }.sortedByDescending { it.importeTotal }
    }

    private fun agruparPorVendedor(ventas: List<Venta>): List<VentaAgrupada> {
        val dict = mutableMapOf<String, Triple<Double, Double, Int>>()

        for (venta in ventas) {
            val key = venta.facturaClienteVendedorNombre ?: "Sin vendedor"
            val current = dict[key] ?: Triple(0.0, 0.0, 0)
            dict[key] = Triple(
                current.first + (venta.cantidad * venta.precioUnitario),
                current.second + venta.cantidad,
                current.third + 1
            )
        }

        return dict.map { (nombre, triple) ->
            VentaAgrupada(
                nombre = nombre,
                importeTotal = triple.first,
                cantidadUnidades = triple.second,
                cantidadVentas = triple.third
            )
        }.sortedByDescending { it.importeTotal }
    }

    private fun agruparPorProveedor(ventas: List<Venta>): List<VentaAgrupada> {
        val dict = mutableMapOf<String, Triple<Double, Double, Int>>()

        for (venta in ventas) {
            val key = venta.articuloProveedorNombre ?: "Sin proveedor"
            val current = dict[key] ?: Triple(0.0, 0.0, 0)
            dict[key] = Triple(
                current.first + (venta.cantidad * venta.precioUnitario),
                current.second + venta.cantidad,
                current.third + 1
            )
        }

        return dict.map { (nombre, triple) ->
            VentaAgrupada(
                nombre = nombre,
                importeTotal = triple.first,
                cantidadUnidades = triple.second,
                cantidadVentas = triple.third
            )
        }.sortedByDescending { it.importeTotal }
    }

    private fun agruparPorFecha(ventas: List<Venta>): List<VentaAgrupada> {
        val dict = mutableMapOf<String, Triple<Double, Double, Int>>()

        for (venta in ventas) {
            // Usar fechaFormateada que ya convierte de yyyy-MM-dd a dd/MM/yyyy
            val key = venta.fechaFormateada
            val current = dict[key] ?: Triple(0.0, 0.0, 0)
            dict[key] = Triple(
                current.first + (venta.cantidad * venta.precioUnitario),
                current.second + venta.cantidad,
                current.third + 1
            )
        }

        // Ordenar por fecha descendente (más reciente primero)
        // Necesitamos parsear las fechas para ordenarlas correctamente
        return dict.map { (nombre, triple) ->
            VentaAgrupada(
                nombre = nombre,
                importeTotal = triple.first,
                cantidadUnidades = triple.second,
                cantidadVentas = triple.third
            )
        }.sortedByDescending { ventaAgrupada ->
            // Convertir dd/MM/yyyy a un valor comparable (yyyy-MM-dd)
            try {
                val parts = ventaAgrupada.nombre.split("/")
                if (parts.size == 3) {
                    "${parts[2]}-${parts[1]}-${parts[0]}"
                } else {
                    ventaAgrupada.nombre
                }
            } catch (e: Exception) {
                ventaAgrupada.nombre
            }
        }
    }
}
