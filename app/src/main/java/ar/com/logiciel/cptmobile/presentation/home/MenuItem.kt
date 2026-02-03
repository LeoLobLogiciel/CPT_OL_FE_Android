package ar.com.logiciel.cptmobile.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange

/**
 * Sealed class representing each menu item in the navigation drawer.
 * Matches the structure from iOS SideMenuView.swift
 */
sealed class MenuItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val section: String? = null
) {
    data object PedidosPanel : MenuItem(
        route = "pedidos",
        title = "Panel",
        icon = Icons.Default.DateRange, // Closest to shippingbox.fill
        color = Color(0xFF2196F3), // Blue
        section = "Pedidos"
    )

    data object ClientesPanel : MenuItem(
        route = "clientes_panel",
        title = "Panel",
        icon = Icons.Default.Person, // person.3.fill equivalent
        color = Color(0xFF4CAF50), // Green
        section = "Clientes y Ventas"
    )

    data object ClientesTablero : MenuItem(
        route = "tablero",
        title = "Tablero",
        icon = Icons.Default.AccountCircle, // chart.bar.fill equivalent (using closest available)
        color = Color(0xFF9C27B0), // Purple
        section = "Clientes y Ventas"
    )

    data object ClientesVentas : MenuItem(
        route = "ventas",
        title = "Ventas",
        icon = Icons.Default.ShoppingCart, // cart.fill
        color = Color(0xFF4CAF50), // Green
        section = "Clientes y Ventas"
    )

    data object Utilitarios : MenuItem(
        route = "profile",
        title = "Configuración",
        icon = Icons.Default.Settings, // gearshape.fill
        color = Color.Gray,
        section = "Utilitarios"
    )

    data object Logout : MenuItem(
        route = "logout",
        title = "Cerrar Sesión",
        icon = Icons.Default.ExitToApp, // rectangle.portrait.and.arrow.right
        color = Color.Red,
        section = null
    )

    companion object {
        fun getAllItems(): List<MenuItem> = listOf(
            PedidosPanel,
            ClientesPanel,
            ClientesTablero,
            ClientesVentas,
            Utilitarios,
            Logout
        )

        fun getItemsBySection(): Map<String?, List<MenuItem>> {
            return getAllItems().groupBy { it.section }
        }
    }
}
