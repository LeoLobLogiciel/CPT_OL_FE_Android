package ar.com.logiciel.cptmobile.domain.usecase.ventas

import ar.com.logiciel.cptmobile.domain.model.Venta
import ar.com.logiciel.cptmobile.domain.model.VentasResumen
import javax.inject.Inject

/**
 * Caso de uso para calcular el resumen de ventas
 */
class CalcularResumenUseCase @Inject constructor() {

    operator fun invoke(ventas: List<Venta>): VentasResumen {
        return VentasResumen.fromVentas(ventas)
    }
}
