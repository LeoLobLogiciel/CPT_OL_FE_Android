package ar.com.logiciel.cptmobile.presentation.pedidos.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.PedidoColumna

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosColumnasSheet(
    columnasSeleccionadas: List<PedidoColumna>,
    onToggleColumna: (PedidoColumna) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Seleccionar Columnas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Row {
                    IconButton(onClick = onReset) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Restablecer"
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                }
            }
            
            Text(
                text = "${columnasSeleccionadas.size} de ${PedidoColumna.entries.size} seleccionadas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider()
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(PedidoColumna.entries.toList()) { columna ->
                    val isSelected = columnasSeleccionadas.contains(columna)
                    
                    ListItem(
                        headlineContent = { Text(columna.label) },
                        leadingContent = {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { onToggleColumna(columna) }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            HorizontalDivider()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aceptar")
            }
        }
    }
}
