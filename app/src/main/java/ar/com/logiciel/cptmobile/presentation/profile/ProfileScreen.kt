package ar.com.logiciel.cptmobile.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material3.AlertDialog
import ar.com.logiciel.cptmobile.BuildConfig
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ar.com.logiciel.cptmobile.presentation.navigation.Screen
import timber.log.Timber

/**
 * Profile and configuration screen.
 * Based on iOS ProfileView.swift
 */
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsState()
    val isLoggingOut by viewModel.isLoggingOut.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    var showThemeDropdown by remember { mutableStateOf(false) }

    // Navigate to login when user is logged out
    LaunchedEffect(currentUser) {
        if (currentUser == null && !isLoggingOut) {
            Timber.d("🔄 User logged out, navigating to login screen")
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mi Cuenta Section
        Text(
            text = "Mi Cuenta",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile avatar placeholder
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.primary
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = currentUser?.let { "${it.nombre} ${it.apellido}" } ?: "Usuario",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentUser?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Configuración Section
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column {
                // Versión
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Versión",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                // Tema
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { showThemeDropdown = true },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = when (themeMode) {
                                ar.com.logiciel.cptmobile.core.di.ThemeMode.LIGHT -> Icons.Default.LightMode
                                ar.com.logiciel.cptmobile.core.di.ThemeMode.DARK -> Icons.Default.DarkMode
                                ar.com.logiciel.cptmobile.core.di.ThemeMode.SYSTEM -> Icons.Default.SettingsSuggest
                            },
                            contentDescription = "Tema",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Tema",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = when (themeMode) {
                            ar.com.logiciel.cptmobile.core.di.ThemeMode.LIGHT -> "Claro"
                            ar.com.logiciel.cptmobile.core.di.ThemeMode.DARK -> "Oscuro"
                            ar.com.logiciel.cptmobile.core.di.ThemeMode.SYSTEM -> "Sistema"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = showThemeDropdown,
                    onDismissRequest = { showThemeDropdown = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Claro") },
                        leadingIcon = { Icon(Icons.Default.LightMode, null) },
                        onClick = {
                            viewModel.setThemeMode(ar.com.logiciel.cptmobile.core.di.ThemeMode.LIGHT)
                            showThemeDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Oscuro") },
                        leadingIcon = { Icon(Icons.Default.DarkMode, null) },
                        onClick = {
                            viewModel.setThemeMode(ar.com.logiciel.cptmobile.core.di.ThemeMode.DARK)
                            showThemeDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sistema") },
                        leadingIcon = { Icon(Icons.Default.SettingsSuggest, null) },
                        onClick = {
                            viewModel.setThemeMode(ar.com.logiciel.cptmobile.core.di.ThemeMode.SYSTEM)
                            showThemeDropdown = false
                        }
                    )
                }
            }
        }

        // Logout Button
        Button(
            onClick = { viewModel.showLogoutDialog() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            enabled = !isLoggingOut
        ) {
            if (isLoggingOut) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                Text("Cerrar Sesión")
            }
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLogoutDialog() },
            title = {
                Text("Cerrar Sesión")
            },
            text = {
                Text("¿Estás seguro que deseas cerrar sesión?")
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.logout() }
                ) {
                    Text("Cerrar Sesión", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.dismissLogoutDialog() }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
