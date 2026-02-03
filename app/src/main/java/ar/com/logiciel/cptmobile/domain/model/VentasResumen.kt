package ar.com.logiciel.cptmobile.domain.model

/**
 * Modelo de dominio para el resumen de ventas
 */
data class VentasResumen(
    val importeTotal: Double,
    val cantidadUnidades: Double,
    val cantidadVentas: Int,
    val ticketPromedio: Double
) {
    companion object {
        /**
         * Crea un resumen a partir de una lista de ventas
         */
        fun fromVentas(ventas: List<Venta>): VentasResumen {
            val importeTotal = ventas.sumOf { it.neto }
            val cantidadUnidades = ventas.sumOf { it.cantidad }
            val cantidadVentas = ventas.size
            val ticketPromedio = if (cantidadVentas > 0) importeTotal / cantidadVentas else 0.0

            return VentasResumen(
                importeTotal = importeTotal,
                cantidadUnidades = cantidadUnidades,
                cantidadVentas = cantidadVentas,
                ticketPromedio = ticketPromedio
            )
        }
    }
}
