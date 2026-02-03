package ar.com.logiciel.cptmobile.domain.model

/**
 * Enum para los tipos de agrupación de ventas
 */
enum class TipoAgrupacion(val displayName: String) {
    DETALLE("Detalle"),
    POR_CLIENTE("Por cliente"),
    POR_ARTICULO("Por artículo"),
    POR_RUBRO("Por rubro"),
    POR_VENDEDOR("Por vendedor"),
    POR_PROVEEDOR("Por proveedor"),
    POR_FECHA("Por fecha");

    companion object {
        fun fromDisplayName(name: String): TipoAgrupacion? {
            return entries.find { it.displayName == name }
        }
    }
}
