package ar.com.logiciel.cptmobile.presentation.tablero.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.formatearMoneda
import ar.com.logiciel.cptmobile.domain.model.tablero.DeudaClientesPorZona

@Composable
fun DeudaClientesWidget(
    deudaClientes: List<DeudaClientesPorZona>,
    totalDeuda: Double,
    totalVencida: Double,
    porcentajeVencida: Double,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    
    WidgetCard(
        title = "Deuda clientes",
        icon = Icons.Default.AttachMoney,
        color = Color(0xFFFF9800), // Orange
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
            StatRow(label = "Deuda Total", value = formatearMoneda(totalDeuda))
            StatRow(label = "Vencido", value = formatearMoneda(totalVencida))
            StatRow(label = "% Vencido", value = String.format("%.1f%%", porcentajeVencida))
        },
        expandedContent = {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Gráfico") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Detalle") })
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            when (selectedTab) {
                0 -> {
                    if (deudaClientes.isNotEmpty()) {
                        SimplePieChart(
                            data = deudaClientes.map { it.nombreGrupo to it.saldoActual },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = "No hay datos para mostrar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                1 -> {
                    if (deudaClientes.isNotEmpty()) {
                        DeudaClientesTable(deudaClientes, totalDeuda, totalVencida, porcentajeVencida)
                    } else {
                        Text(
                            text = "No hay datos para mostrar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun DeudaClientesTable(
    deudaClientes: List<DeudaClientesPorZona>,
    totalDeuda: Double,
    totalVencida: Double,
    porcentajeVencida: Double
) {
    Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        // Header
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Grupo", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
            Text("Deuda", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            Text("Vencido", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            Text("%", fontWeight = FontWeight.Bold, modifier = Modifier.width(50.dp))
            Text("Ch.cartera", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            Text("Prom.Pagos", fontWeight = FontWeight.Bold, modifier = Modifier.width(80.dp))
        }
        
        HorizontalDivider()
        
        deudaClientes.forEach { zona ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(zona.nombreGrupo, modifier = Modifier.width(120.dp))
                Text(formatearMoneda(zona.saldoActual), modifier = Modifier.width(100.dp))
                Text(formatearMoneda(zona.saldoVencido), modifier = Modifier.width(100.dp))
                Text(String.format("%.2f", zona.porcentajeVencido ?: 0.0), modifier = Modifier.width(50.dp))
                Text(formatearMoneda(zona.chequesVigentes), modifier = Modifier.width(100.dp))
                Text(String.format("%.0f", zona.promedioPagos * 100), modifier = Modifier.width(80.dp))
            }
        }
        
        HorizontalDivider()
        
        // Total row
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Total:", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
            Text(formatearMoneda(totalDeuda), fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            Text(formatearMoneda(totalVencida), fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            Text(String.format("%.0f", porcentajeVencida), fontWeight = FontWeight.Bold, modifier = Modifier.width(50.dp))
            val totalCheques = deudaClientes.sumOf { it.chequesVigentes }
            Text(formatearMoneda(totalCheques), fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            val avgPagos = deudaClientes.map { it.promedioPagos }.average()
            Text(String.format("%.0f", avgPagos * 100), fontWeight = FontWeight.Bold, modifier = Modifier.width(80.dp))
        }
    }
}
