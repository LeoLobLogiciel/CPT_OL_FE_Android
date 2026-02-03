package ar.com.logiciel.cptmobile.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Pedidos : Screen("pedidos")
    data object ClientesPanel : Screen("clientes_panel")
    data object Tablero : Screen("tablero")
    data object Ventas : Screen("ventas")
    data object Profile : Screen("profile")
}
