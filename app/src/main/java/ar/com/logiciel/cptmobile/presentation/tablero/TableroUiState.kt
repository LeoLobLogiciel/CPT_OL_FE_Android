package ar.com.logiciel.cptmobile.presentation.tablero

import ar.com.logiciel.cptmobile.domain.model.tablero.*
import java.time.LocalDate

/**
 * Estado de UI para el Tablero Principal
 * Cada widget tiene sus propios filtros de fecha (persistidos)
 */
data class TableroUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Fechas para cada widget (persistidas individualmente)
    // Por defecto: primer día del mes actual hasta hoy
    val ventasWebFechaDesde: LocalDate = LocalDate.now().withDayOfMonth(1),
    val ventasWebFechaHasta: LocalDate = LocalDate.now(),
    
    val ventasFechaDesde: LocalDate = LocalDate.now().withDayOfMonth(1),
    val ventasFechaHasta: LocalDate = LocalDate.now(),
    
    val ventasRubroFechaDesde: LocalDate = LocalDate.now().withDayOfMonth(1),
    val ventasRubroFechaHasta: LocalDate = LocalDate.now(),

    // Estados de expansión de widgets (persistidos)
    val ventasWebExpanded: Boolean = true,
    val ventasZonaExpanded: Boolean = true,
    val deudaClientesExpanded: Boolean = true,
    val ventasRubroExpanded: Boolean = true,
    val deudaProveedoresExpanded: Boolean = true,
    val resmasExpanded: Boolean = true,

    // Datos
    val ventasWebML: List<VentasWebML> = emptyList(),
    val ventasWebEmpresas: VentasWebEmpresas? = null,
    val ventasPorZona: List<VentaPorZona> = emptyList(),
    val deudaClientes: List<DeudaClientesPorZona> = emptyList(),
    val ventasPorRubro: List<VentaPorRubro> = emptyList(),
    val ventaMLPorRubro: List<VentaPorRubro> = emptyList(),
    val deudaProveedores: List<DeudaProveedor> = emptyList(),
    val resmas: List<Resma> = emptyList()
) {
    // Computed properties - Ventas Web ML
    val totalVentasWebML: Double get() = ventasWebML.sumOf { it.netoTotalValue }
    val totalCantidadWebML: Int get() = ventasWebML.sumOf { it.cantidad }

    // Computed properties - Venta por Zona
    val totalVentasPorZona: Double get() = ventasPorZona.sumOf { it.netoTotalFacturado }
    val totalClientesNuevos: Int get() = ventasPorZona.sumOf { it.cantidadClientesNuevos }
    val totalClientesAtendidos: Int get() = ventasPorZona.sumOf { it.cantidadClientesAtendidos }

    // Computed properties - Deuda Clientes
    val totalDeudaClientes: Double get() = deudaClientes.sumOf { it.saldoActual }
    val totalDeudaVencida: Double get() = deudaClientes.sumOf { it.saldoVencido }
    val porcentajeDeudaVencida: Double
        get() = if (totalDeudaClientes > 0) (totalDeudaVencida / totalDeudaClientes) * 100 else 0.0

    // Computed properties - Deuda Proveedores
    val totalDeudaProveedores: Double get() = deudaProveedores.sumOf { it.saldo }

    // Computed properties - Resmas
    val totalStockResmas: Int get() = resmas.sumOf { it.stockActual }
    val totalComprometidoResmas: Int get() = resmas.sumOf { it.stockComprometido }
    val totalDisponibleResmas: Int get() = resmas.sumOf { it.stockDisponible }
}
