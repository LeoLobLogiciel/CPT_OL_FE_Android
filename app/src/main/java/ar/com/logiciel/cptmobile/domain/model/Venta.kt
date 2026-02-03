package ar.com.logiciel.cptmobile.domain.model

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Modelo de dominio para una venta (FacturaCliente Detalle)
 */
data class Venta(
    val id: Int,
    val facturaClienteFechaEmision: String,
    val facturaClienteTipo: String,
    val facturaClienteNumeroCompleto: String,
    val articuloNombre: String?,
    val articuloArticuloRubroNombre: String?,
    val articuloProveedorNombre: String?,
    val cantidad: Double,
    val precioUnitario: Double,
    val facturaClienteClienteNombre: String,
    val facturaClienteVendedorNombre: String?
) {
    /**
     * Formato de fecha de yyyy-MM-dd a dd/MM/yyyy
     */
    val fechaFormateada: String
        get() {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = inputFormat.parse(facturaClienteFechaEmision.substring(0, 10))
                date?.let { outputFormat.format(it) } ?: facturaClienteFechaEmision
            } catch (e: Exception) {
                facturaClienteFechaEmision
            }
        }

    val factura: String
        get() = facturaClienteNumeroCompleto

    /**
     * Neto = Cantidad * PrecioUnitario
     */
    val neto: Double
        get() = cantidad * precioUnitario

    /**
     * Total = Neto * 1.21 (asumiendo IVA 21%)
     */
    val total: Double
        get() = neto * 1.21
}
