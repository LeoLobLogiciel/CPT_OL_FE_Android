package ar.com.logiciel.cptmobile.domain.model

enum class PedidoColumna(val label: String) {
    DOCUMENTOS("Documentos"),
    CLIENTE("Cliente"),
    NOMBRE_VENDEDOR("Vendedor"),
    COMENTARIOS("Comentarios"),
    CUIT_CLIENTE("C.U.I.T."),
    FECHA_CREACION("Creado"),
    CANTIDAD_ITEMS("Cant.Items"),
    CANTIDAD_UNIDADES("Cant.Unidades"),
    CLIENTE_FECHA_PRIMERA_VENTA("Primera venta"),
    CLIENTE_FECHA_ULTIMA_VENTA("Última venta"),
    CLIENTE_FECHA_ALTA("Alta cliente"),
    ORDEN_COMPRA("Orden de compra"),
    TOTAL("Total"),
    TOPE_CREDITO_CLIENTE("Tope crédito"),
    SALDO_ACTUAL("Saldo actual"),
    SALDO_VENCIDO("Saldo vencido"),
    CLIENTE_CHEQUES_VIGENTES("Cheques vigentes"),
    PROMEDIO_PAGOS("Promedio pagos"),
    PROMEDIO_ULTIMO_PAGO("Promedio últ. pagos"),
    CONFIRMADO("Confirmado"),
    FACTURABLE("Facturable"),
    CERRADO("Cerrado"),
    ESTADO_COMERCIAL("Comercial"),
    ESTADO_CREDITICIO("Crediticia"),
    INDIVISIBLE("Indivisible"),
    ES_PRIMERO("Es 1ero."),
    SOLO_REMITO("S.Rem"),
    R1("R 1"),
    SOLO_FACTURA("S.Fac"),
    BACKORDER("Backorder"),
    USO_INTERNO("Uso Interno"),
    PICKING_LIST_IMPRESO("P.L.Impreso"),
    ETIQUETAS_IMPRESAS("Et.Impresas"),
    CANTIDAD_BULTOS("Cant.Bultos"),
    MERCADO_LIBRE("M.L."),
    FACTURA("Factura"),
    REMITO("Remito"),
    CONDICION_VENTA("Cond.Venta"),
    USUARIO("Usuario"),
    PERCEPCIONES("Percepciones"),
    NETO("Neto"),
    IVA("I.V.A."),
    ID_MERCADOLIBRE("IdMercadolibre"),
    DOMICILIO_ENTREGA("Dom.Ent."),
    SECTOR_ENTREGA("Sector Ent.");

    companion object {
        val columnasDefault: List<PedidoColumna> = listOf(
            CLIENTE,
            NOMBRE_VENDEDOR,
            FECHA_CREACION,
            CANTIDAD_ITEMS,
            TOTAL,
            CONFIRMADO,
            ESTADO_COMERCIAL,
            ESTADO_CREDITICIO,
            BACKORDER,
            MERCADO_LIBRE
        )

        fun getValor(pedido: PedidoPanelCompleto, columna: PedidoColumna): String {
            return when (columna) {
                DOCUMENTOS -> pedido.cantidadDocumentos?.toString() ?: "0"
                CLIENTE -> pedido.nombreCliente
                NOMBRE_VENDEDOR -> pedido.nombreVendedor
                COMENTARIOS -> pedido.comentarios ?: ""
                CUIT_CLIENTE -> pedido.cliente.cuit ?: ""
                FECHA_CREACION -> pedido.fechaCreacionFormateada
                CANTIDAD_ITEMS -> pedido.cantidadItems.toString()
                CANTIDAD_UNIDADES -> pedido.cantidadUnidades.toString()
                CLIENTE_FECHA_PRIMERA_VENTA -> pedido.clienteFechaPrimeraVentaFormateada
                CLIENTE_FECHA_ULTIMA_VENTA -> pedido.clienteFechaUltimaVentaFormateada
                CLIENTE_FECHA_ALTA -> pedido.clienteFechaAltaFormateada
                ORDEN_COMPRA -> pedido.ordenCompra ?: ""
                TOTAL -> formatearMoneda(pedido.total)
                TOPE_CREDITO_CLIENTE -> formatearMoneda(pedido.cliente.topeCredito ?: 0.0)
                SALDO_ACTUAL -> formatearMoneda(pedido.cliente.saldoActual ?: 0.0)
                SALDO_VENCIDO -> formatearMoneda(pedido.cliente.saldoVencido ?: 0.0)
                CLIENTE_CHEQUES_VIGENTES -> formatearMoneda(pedido.cliente.chequesVigentes ?: 0.0)
                PROMEDIO_PAGOS -> "${pedido.cliente.promedioPagos?.toInt() ?: 0} días"
                PROMEDIO_ULTIMO_PAGO -> "${pedido.cliente.promedioUltimoPago?.toInt() ?: 0} días"
                CONFIRMADO -> if (pedido.confirmado) "✅" else "❌"
                FACTURABLE -> if (pedido.facturable == true) "✅" else "❌"
                CERRADO -> if (pedido.cerrado == true) "✅" else "❌"
                ESTADO_COMERCIAL -> pedido.estadoComercialTexto
                ESTADO_CREDITICIO -> pedido.estadoCrediticioTexto
                INDIVISIBLE -> if (pedido.indivisible == true) "✅" else "❌"
                ES_PRIMERO -> if (pedido.esPrimero == true) "✅" else "❌"
                SOLO_REMITO -> if (pedido.soloRemito == true) "✅" else "❌"
                R1 -> if (pedido.r1 == true) "✅" else "❌"
                SOLO_FACTURA -> if (pedido.soloFactura == true) "✅" else "❌"
                BACKORDER -> if (pedido.backOrder == true) "✅" else "❌"
                USO_INTERNO -> if (pedido.usoInterno == true) "✅" else "❌"
                PICKING_LIST_IMPRESO -> if (pedido.pickingListImpreso == true) "✅" else "❌"
                ETIQUETAS_IMPRESAS -> if (pedido.etiquetasImpresas == true) "✅" else "❌"
                CANTIDAD_BULTOS -> pedido.cantidadBultos?.toString() ?: "0"
                MERCADO_LIBRE -> if (pedido.generadoPorMercadoLibre == true) "✅" else "❌"
                FACTURA -> pedido.facturaClienteNumeroCompleto ?: ""
                REMITO -> pedido.remitoClienteNumeroCompleto ?: ""
                CONDICION_VENTA -> pedido.condicionVentaNombre ?: ""
                USUARIO -> pedido.usuarioNombre ?: ""
                PERCEPCIONES -> formatearMoneda(pedido.facturaClientePercepciones?.toDoubleOrNull() ?: 0.0)
                NETO -> formatearMoneda(pedido.neto)
                IVA -> formatearMoneda(pedido.iva)
                ID_MERCADOLIBRE -> pedido.idMercadolibre?.toString() ?: ""
                DOMICILIO_ENTREGA -> pedido.domicilioEntrega ?: ""
                SECTOR_ENTREGA -> pedido.sectorEntrega ?: ""
            }
        }
    }
}
