package ar.com.logiciel.cptmobile.domain.model.tablero

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VentaPorRubro(
    @Json(name = "rubroNombre") val rubroNombre: String,
    @Json(name = "totalVentas") val totalVentas: Double?,
    @Json(name = "cantidadUnidades") val cantidadUnidadesString: String?,
    @Json(name = "gananciaTotalLogiciel") val gananciaTotalLogiciel: Double?,
    @Json(name = "margenTotalLogiciel") val margenTotalLogiciel: String?
) {
    val cantidadUnidades: Int get() = cantidadUnidadesString?.toIntOrNull() ?: 0
    val id: String get() = rubroNombre
}

@JsonClass(generateAdapter = true)
data class VentaPorRubroData(
    @Json(name = "ventaPorRubro") val ventaPorRubro: List<VentaPorRubro>?,
    @Json(name = "ventaMLPorRubro") val ventaMLPorRubro: List<VentaPorRubro>?
)

@JsonClass(generateAdapter = true)
data class VentaPorRubroResponse(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: VentaPorRubroData?
)
