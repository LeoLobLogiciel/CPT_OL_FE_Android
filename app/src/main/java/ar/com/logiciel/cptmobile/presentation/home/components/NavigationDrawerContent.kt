package ar.com.logiciel.cptmobile.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.presentation.home.MenuItem

/**
 * Navigation drawer content that displays menu sections and items.
 * Based on iOS SideMenuView.swift structure.
 */
@Composable
fun NavigationDrawerContent(
    selectedItem: MenuItem,
    userName: String?,
    onItemSelected: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(280.dp)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            // Header
            DrawerHeader(userName = userName)

            Divider()

            // Menu items
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 10.dp)
            ) {
                val itemsBySection = MenuItem.getItemsBySection()

                // Pedidos Section
                MenuSection(title = "Pedidos") {
                    MenuItemRow(
                        item = MenuItem.PedidosPanel,
                        isSelected = selectedItem == MenuItem.PedidosPanel,
                        onClick = { onItemSelected(MenuItem.PedidosPanel) }
                    )
                }

                // Clientes y Ventas Section
                MenuSection(title = "Clientes y Ventas") {
                    MenuItemRow(
                        item = MenuItem.ClientesTablero,
                        isSelected = selectedItem == MenuItem.ClientesTablero,
                        onClick = { onItemSelected(MenuItem.ClientesTablero) }
                    )
                    MenuItemRow(
                        item = MenuItem.ClientesVentas,
                        isSelected = selectedItem == MenuItem.ClientesVentas,
                        onClick = { onItemSelected(MenuItem.ClientesVentas) }
                    )
                }

                // Utilitarios Section
                MenuSection(title = "Utilitarios") {
                    MenuItemRow(
                        item = MenuItem.Utilitarios,
                        isSelected = selectedItem == MenuItem.Utilitarios,
                        onClick = { onItemSelected(MenuItem.Utilitarios) }
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 10.dp))

                // Logout
                MenuItemRow(
                    item = MenuItem.Logout,
                    isSelected = false,
                    onClick = { onItemSelected(MenuItem.Logout) }
                )
            }
        }
    }
}

@Composable
private fun DrawerHeader(
    userName: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 20.dp, end = 20.dp, bottom = 30.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile avatar placeholder
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "CPT Mobile",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Sistema de Gestión",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MenuSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
        content()
    }
}

@Composable
private fun MenuItemRow(
    item: MenuItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) item.color else Color.Transparent
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
    val iconColor = if (isSelected) Color.White else item.color

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            modifier = Modifier.size(24.dp),
            tint = iconColor
        )

        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Color.White
            )
        }
    }
}
