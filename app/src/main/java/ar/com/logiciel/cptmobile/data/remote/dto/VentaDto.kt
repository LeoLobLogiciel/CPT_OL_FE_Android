package ar.com.logiciel.cptmobile.data.remote.dto

import ar.com.logiciel.cptmobile.domain.model.Venta
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VentaDto(
    @Json(name = "Id")
    val id: Int,

    @Json(name = "FacturaCliente_FechaEmision")
    val facturaClienteFechaEmision: String,

    @Json(name = "FacturaCliente_Tipo")
    val facturaClienteTipo: String,

    @Json(name = "FacturaCliente_NumeroCompleto")
    val facturaClienteNumeroCompleto: String,

    @Json(name = "Articulo_Nombre")
    val articuloNombre: String?,

    @Json(name = "Articulo_ArticuloRubro_Nombre")
    val articuloArticuloRubroNombre: String?,

    @Json(name = "Articulo_Proveedor_Nombre")
    val articuloProveedorNombre: String?,

    @Json(name = "Cantidad")
    val cantidad: Double,

    @Json(name = "PrecioUnitario")
    val precioUnitario: Double,

    @Json(name = "FacturaCliente_Cliente_Nombre")
    val facturaClienteClienteNombre: String,

    @Json(name = "FacturaCliente_Vendedor_Nombre")
    val facturaClienteVendedorNombre: String?
)

@JsonClass(generateAdapter = true)
data class VentasResponse(
    @Json(name = "status")
    val status: String,

    @Json(name = "data")
    val data: List<VentaDto>?
)

fun VentaDto.toDomainModel(): Venta {
    return Venta(
        id = id,
        facturaClienteFechaEmision = facturaClienteFechaEmision,
        facturaClienteTipo = facturaClienteTipo,
        facturaClienteNumeroCompleto = facturaClienteNumeroCompleto,
        articuloNombre = articuloNombre,
        articuloArticuloRubroNombre = articuloArticuloRubroNombre,
        articuloProveedorNombre = articuloProveedorNombre,
        cantidad = cantidad,
        precioUnitario = precioUnitario,
        facturaClienteClienteNombre = facturaClienteClienteNombre,
        facturaClienteVendedorNombre = facturaClienteVendedorNombre
    )
}
