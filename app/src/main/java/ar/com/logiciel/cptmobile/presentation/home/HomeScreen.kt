package ar.com.logiciel.cptmobile.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ar.com.logiciel.cptmobile.presentation.clientes.ClientesPanelScreen
import ar.com.logiciel.cptmobile.presentation.home.components.NavigationDrawerContent
import ar.com.logiciel.cptmobile.presentation.navigation.Screen
import ar.com.logiciel.cptmobile.presentation.pedidos.PedidosScreen
import ar.com.logiciel.cptmobile.presentation.profile.ProfileScreen
import ar.com.logiciel.cptmobile.presentation.tablero.TableroScreen
import ar.com.logiciel.cptmobile.presentation.ventas.VentasScreen
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Home screen with navigation drawer and multiple sections.
 * Based on iOS HomeView.swift structure.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val selectedMenuItem by viewModel.selectedMenuItem.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showLogoutDialog by remember { mutableStateOf(false) }

    // Navigate to login when user is logged out
    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            Timber.d("🔄 User logged out from HomeScreen, navigating to login")
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                selectedItem = selectedMenuItem,
                userName = currentUser?.fullName,
                onItemSelected = { item ->
                    when (item) {
                        MenuItem.Logout -> {
                            Timber.d("🚪 Logout option selected from drawer")
                            showLogoutDialog = true
                        }
                        else -> {
                            viewModel.selectMenuItem(item)
                        }
                    }
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = viewModel.getCurrentScreenTitle(),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                Timber.d("🍔 Hamburger menu clicked")
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        // Profile avatar
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable {
                                    Timber.d("👤 Profile icon clicked")
                                    viewModel.selectMenuItem(MenuItem.Utilitarios)
                                },
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Log del item seleccionado
                LaunchedEffect(selectedMenuItem) {
                    Timber.d("🎯 HomeScreen - Mostrando contenido para: ${selectedMenuItem.title}")
                }

                // Content based on selected menu item
                when (selectedMenuItem) {
                    MenuItem.PedidosPanel -> PedidosScreen()
                    MenuItem.ClientesPanel -> ClientesPanelScreen()
                    MenuItem.ClientesTablero -> TableroScreen()
                    MenuItem.ClientesVentas -> VentasScreen()
                    MenuItem.Utilitarios -> ProfileScreen(navController = navController)
                    MenuItem.Logout -> { /* Handled by dialog */ }
                }
            }
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        // Navigate to profile screen which has the logout functionality
        LaunchedEffect(Unit) {
            viewModel.selectMenuItem(MenuItem.Utilitarios)
            showLogoutDialog = false
        }
    }
}
