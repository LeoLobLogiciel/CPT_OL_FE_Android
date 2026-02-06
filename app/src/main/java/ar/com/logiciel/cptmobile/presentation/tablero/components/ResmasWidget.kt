package ar.com.logiciel.cptmobile.presentation.tablero.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.tablero.Resma

@Composable
fun ResmasWidget(
    resmas: List<Resma>,
    totalStock: Int,
    totalComprometido: Int,
    totalDisponible: Int,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    WidgetCard(
        title = "Stock Resmas",
        icon = Icons.Default.Inventory,
        color = Color(0xFF795548), // Brown
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
            StatRow(label = "Stock Total", value = totalStock.toString())
            StatRow(label = "Comprometido", value = totalComprometido.toString())
            StatRow(label = "Disponible", value = totalDisponible.toString())
        },
        expandedContent = {
            if (resmas.isNotEmpty()) {
                Column {
                    resmas.forEach { resma ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = resma.nombre,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "SKU: ${resma.sku}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Stock: ${resma.stockActual}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Comp: ${resma.stockComprometido}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                val disponibleColor = if (resma.stockDisponible >= 0) 
                                    Color(0xFF4CAF50) else Color.Red
                                Text(
                                    text = "Disp: ${resma.stockDisponible}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = disponibleColor
                                )
                            }
                        }
                        if (resma.id != resmas.lastOrNull()?.id) {
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
