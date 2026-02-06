package ar.com.logiciel.cptmobile.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@JsonClass(generateAdapter = true)
data class PedidoPanelResponse(
    @Json(name = "status") val status: String,
    @Json(name = "errors") val errors: List<String>,
    @Json(name = "data") val data: List<PedidoPanelCompleto>,
    @Json(name = "mode") val mode: String
)

@JsonClass(generateAdapter = true)
data class ClienteInfo(
    @Json(name = "Id") val id: Int,
    @Json(name = "Nombre") val nombre: String,
    @Json(name = "CUIT") val cuit: String?,
    @Json(name = "SaldoActual") val saldoActual: Double?,
    @Json(name = "SaldoVencido") val saldoVencido: Double?,
    @Json(name = "TopeCredito") val topeCredito: Double?,
    @Json(name = "FechaPrimeraVenta") val fechaPrimeraVenta: String?,
    @Json(name = "FechaUltimaVenta") val fechaUltimaVenta: String?,
    @Json(name = "FechaAlta") val fechaAlta: String?,
    @Json(name = "PromedioPagos") val promedioPagos: Double?,
    @Json(name = "PromedioUltimoPago") val promedioUltimoPago: Double?,
    @Json(name = "ChequesVigentes") val chequesVigentes: Double?,
    @Json(name = "Vendedor") val vendedor: VendedorInfo?
)

@JsonClass(generateAdapter = true)
data class VendedorInfo(
    @Json(name = "Id") val id: Int,
    @Json(name = "Nombre") val nombre: String
)

@JsonClass(generateAdapter = true)
data class PedidoPanelCompleto(
    @Json(name = "Id") val id: Int,
    @Json(name = "Numero") val numero: String,
    @Json(name = "Cliente") val cliente: ClienteInfo,
    
    // Campos planos
    @Json(name = "CondicionVenta_Id") val condicionVentaId: Int?,
    @Json(name = "CondicionVenta_Nombre") val condicionVentaNombre: String?,
    @Json(name = "Usuario_Id") val usuarioId: Int?,
    @Json(name = "Usuario_Nombre") val usuarioNombre: String?,
    @Json(name = "FacturaCliente_Id") val facturaClienteId: Int?,
    @Json(name = "FacturaCliente_NumeroCompleto") val facturaClienteNumeroCompleto: String?,
    @Json(name = "FacturaCliente_Percepciones") val facturaClientePercepciones: String?,
    @Json(name = "RemitoCliente_Id") val remitoClienteId: Int?,
    @Json(name = "RemitoCliente_NumeroCompleto") val remitoClienteNumeroCompleto: String?,
    
    // Fechas
    @Json(name = "CreatedAt") val createdAt: String?,
    @Json(name = "UpdatedAt") val updatedAt: String?,
    @Json(name = "FechaCreacion") val fechaCreacion: String,
    @Json(name = "FechaEntrega") val fechaEntrega: String?,
    @Json(name = "FechaVencimiento") val fechaVencimiento: String?,
    
    // Datos comerciales
    @Json(name = "Comentarios") val comentarios: String?,
    @Json(name = "OrdenCompra") val ordenCompra: String?,
    @Json(name = "DomicilioEntrega") val domicilioEntrega: String?,
    @Json(name = "SectorEntrega") val sectorEntrega: String?,
    @Json(name = "Neto") val neto: Double,
    @Json(name = "IVA") val iva: Double,
    @Json(name = "Total") val total: Double,
    @Json(name = "MargenLogiciel") val margenLogiciel: Double?,
    @Json(name = "MargenReal") val margenReal: Double?,
    @Json(name = "GananciaLogiciel") val gananciaLogiciel: Double?,
    @Json(name = "GananciaReal") val gananciaReal: Double?,
    @Json(name = "CantidadItems") val cantidadItems: Int,
    @Json(name = "CantidadUnidades") val cantidadUnidades: Int,
    
    // Estados booleanos
    @Json(name = "Confirmado") val confirmado: Boolean,
    @Json(name = "AprobacionComercial") val aprobacionComercial: Boolean?,
    @Json(name = "RechazoComercial") val rechazoComercial: Boolean?,
    @Json(name = "MotivoRechazoComercial") val motivoRechazoComercial: String?,
    @Json(name = "AprobacionCrediticio") val aprobacionCrediticio: Boolean?,
    @Json(name = "RechazoCrediticio") val rechazoCrediticio: Boolean?,
    @Json(name = "MotivoRechazoCrediticio") val motivoRechazoCrediticio: String?,
    @Json(name = "Facturable") val facturable: Boolean?,
    @Json(name = "Cerrado") val cerrado: Boolean?,
    @Json(name = "BackOrder") val backOrder: Boolean?,
    @Json(name = "EsPrimero") val esPrimero: Boolean?,
    @Json(name = "RetiraPorMostrador") val retiraPorMostrador: Boolean?,
    @Json(name = "Indivisible") val indivisible: Boolean?,
    @Json(name = "SoloRemito") val soloRemito: Boolean?,
    @Json(name = "SoloFactura") val soloFactura: Boolean?,
    @Json(name = "R1") val r1: Boolean?,
    @Json(name = "UsoInterno") val usoInterno: Boolean?,
    @Json(name = "PickingListImpreso") val pickingListImpreso: Boolean?,
    @Json(name = "EtiquetasImpresas") val etiquetasImpresas: Boolean?,
    @Json(name = "TodosLosArmados") val todosLosArmados: Boolean?,
    @Json(name = "GeneradoPorMercadoLibre") val generadoPorMercadoLibre: Boolean?,
    
    // Otros
    @Json(name = "CantidadBultos") val cantidadBultos: Int?,
    @Json(name = "Armados") val armados: String?,
    @Json(name = "CantidadBultosPorEstacion") val cantidadBultosPorEstacion: String?,
    @Json(name = "IdMercadolibre") val idMercadolibre: Int?,
    @Json(name = "CantidadDocumentos") val cantidadDocumentos: Int?,
    @Json(name = "EstadoGrabacion") val estadoGrabacion: String?
) {
    val nombreCliente: String get() = cliente.nombre
    val nombreVendedor: String get() = cliente.vendedor?.nombre ?: "N/D"
    
    val fechaCreacionFormateada: String get() = formatearFecha(fechaCreacion)
    val fechaEntregaFormateada: String get() = fechaEntrega?.let { formatearFecha(it) } ?: ""
    val clienteFechaPrimeraVentaFormateada: String get() = cliente.fechaPrimeraVenta?.let { formatearFecha(it) } ?: ""
    val clienteFechaUltimaVentaFormateada: String get() = cliente.fechaUltimaVenta?.let { formatearFecha(it) } ?: ""
    val clienteFechaAltaFormateada: String get() = cliente.fechaAlta?.let { formatearFecha(it) } ?: ""
    
    val estadoComercialTexto: String get() = when {
        aprobacionComercial == true -> "✅"
        rechazoComercial == true -> "❌"
        else -> "⏳"
    }
    
    val estadoCrediticioTexto: String get() = when {
        aprobacionCrediticio == true -> "✅"
        rechazoCrediticio == true -> "❌"
        else -> "⏳"
    }
    
    private fun formatearFecha(fecha: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(fecha.take(10))
            date?.let { outputFormat.format(it) } ?: fecha
        } catch (e: Exception) {
            fecha
        }
    }
}

fun formatearMoneda(valor: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
    formatter.currency = java.util.Currency.getInstance("ARS")
    return formatter.format(valor)
}
