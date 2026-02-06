package ar.com.logiciel.cptmobile.presentation.tablero.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.formatearMoneda
import ar.com.logiciel.cptmobile.domain.model.tablero.VentaPorZona
import ar.com.logiciel.cptmobile.presentation.common.DateFilterSheet

@Composable
fun VentasPorZonaWidget(
    ventasPorZona: List<VentaPorZona>,
    totalVentas: Double,
    totalClientesNuevos: Int,
    totalClientesAtendidos: Int,
    fechaDesde: java.time.LocalDate,
    fechaHasta: java.time.LocalDate,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onFechaDesdeChange: (java.time.LocalDate) -> Unit,
    onFechaHastaChange: (java.time.LocalDate) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDateFilter by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    
    WidgetCard(
        title = "Venta",
        icon = Icons.Default.PieChart,
        color = Color.Green,
        isExpanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        headerButtons = {
            IconButton(onClick = { showDateFilter = true }) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Filtro fechas",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
            StatRow(label = "Total Facturado", value = formatearMoneda(totalVentas))
            StatRow(label = "Clientes Nuevos", value = totalClientesNuevos.toString())
            StatRow(label = "Clientes Atendidos", value = totalClientesAtendidos.toString())
        },
        expandedContent = {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Gráfico") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Detalle") })
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            when (selectedTab) {
                0 -> {
                    // Gráfico de sectores simplificado
                    if (ventasPorZona.isNotEmpty()) {
                        SimplePieChart(
                            data = ventasPorZona.map { it.nombreGrupo to it.netoTotalFacturado },
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
                    // Tabla detalle
                    if (ventasPorZona.isNotEmpty()) {
                        HorizontalScrollTable(ventasPorZona, totalVentas, totalClientesNuevos, totalClientesAtendidos)
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
    
    if (showDateFilter) {
        DateFilterSheet(
            title = "Filtro - Venta",
            fechaDesde = fechaDesde,
            fechaHasta = fechaHasta,
            onFechaDesdeChange = onFechaDesdeChange,
            onFechaHastaChange = onFechaHastaChange,
            onApply = {
                showDateFilter = false
                onRefresh()
            },
            onDismiss = { showDateFilter = false }
        )
    }
}

@Composable
private fun HorizontalScrollTable(
    ventasPorZona: List<VentaPorZona>,
    totalVentas: Double,
    totalClientesNuevos: Int,
    totalClientesAtendidos: Int
) {
    Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Grupo", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
            Text("$ Fact.", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            Text("%", fontWeight = FontWeight.Bold, modifier = Modifier.width(40.dp))
            Text("Nuevos", fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp))
            Text("Atendidos", fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp))
            Text("T.prom.", fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            Text("M.prom.", fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp))
        }
        
        HorizontalDivider()
        
        // Data rows
        ventasPorZona.forEach { zona ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(zona.nombreGrupo, modifier = Modifier.width(120.dp))
                Text(formatearMoneda(zona.netoTotalFacturado), modifier = Modifier.width(100.dp))
                Text(String.format("%.0f", zona.porcentajeIncidencia), modifier = Modifier.width(40.dp))
                Text(zona.cantidadClientesNuevos.toString(), modifier = Modifier.width(60.dp))
                Text(zona.cantidadClientesAtendidos.toString(), modifier = Modifier.width(70.dp))
                Text(formatearMoneda(zona.ticketPromedio), modifier = Modifier.width(100.dp))
                Text("${zona.margenLogicielPromedio}%", modifier = Modifier.width(60.dp))
            }
        }
        
        HorizontalDivider()
        
        // Total row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Total:", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
            Text(formatearMoneda(totalVentas), fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            Text("", modifier = Modifier.width(40.dp))
            Text(totalClientesNuevos.toString(), fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp))
            Text(totalClientesAtendidos.toString(), fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp))
            val avgTicket = if (totalClientesAtendidos > 0) totalVentas / totalClientesAtendidos else 0.0
            Text(formatearMoneda(avgTicket), fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            val avgMargen = ventasPorZona.map { it.margenLogicielPromedio.toDoubleOrNull() ?: 0.0 }.average()
            Text(String.format("%.0f%%", avgMargen), fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp))
        }
    }
}
