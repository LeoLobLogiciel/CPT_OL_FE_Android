package ar.com.logiciel.cptmobile.domain.model

/**
 * Modelo de dominio para Cliente
 */
data class Cliente(
    val id: Int,
    val nombre: String,
    val cuit: String?,
    val email: String?,
    val domicilioFiscal: String?,
    val condicionIVA: String?,
    val saldoActual: Double?,
    val activo: Boolean
)
