package ar.com.logiciel.cptmobile.domain.model

/**
 * Modelo de dominio para ventas agrupadas por diferentes criterios
 */
data class VentaAgrupada(
    val nombre: String,
    val importeTotal: Double,
    val cantidadUnidades: Double,
    val cantidadVentas: Int,
    val incidencia: Double = 0.0
) {
    /**
     * Ticket promedio = importeTotal / cantidadVentas
     */
    val ticketPromedio: Double
        get() = if (cantidadVentas > 0) importeTotal / cantidadVentas else 0.0
}
