package ar.com.logiciel.cptmobile.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String?,
    val nombre: String?,
    val apellido: String?,
    val token: String,
    val isActive: Boolean = true
) {
    val fullName: String
        get() = when {
            !nombre.isNullOrBlank() && !apellido.isNullOrBlank() -> "$nombre $apellido"
            !nombre.isNullOrBlank() -> nombre
            !apellido.isNullOrBlank() -> apellido
            else -> username
        }
}
