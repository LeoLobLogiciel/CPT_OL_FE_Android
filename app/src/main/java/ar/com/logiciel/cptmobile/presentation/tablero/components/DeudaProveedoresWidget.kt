package ar.com.logiciel.cptmobile.presentation.tablero.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.formatearMoneda
import ar.com.logiciel.cptmobile.domain.model.tablero.DeudaProveedor

@Composable
fun DeudaProveedoresWidget(
    deudaProveedores: List<DeudaProveedor>,
    totalDeuda: Double,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    WidgetCard(
        title = "Deuda Proveedores",
        icon = Icons.Default.Business,
        color = Color.Red,
        isExpanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        headerButtons = {
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Actualizar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        summaryContent = {
            Spacer(modifier = Modifier.height(8.dp))
            StatRow(label = "Total Deuda", value = formatearMoneda(totalDeuda))
        },
        expandedContent = {
            if (deudaProveedores.isNotEmpty()) {
                Column {
                    deudaProveedores.forEach { proveedor ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = proveedor.nombre,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = formatearMoneda(proveedor.saldo),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                            )
                        }
                        if (proveedor.id != deudaProveedores.lastOrNull()?.id) {
                            HorizontalDivider()
                        }
                    }
                }
            } else {
                Text(
                    text = "No hay datos para mostrar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
