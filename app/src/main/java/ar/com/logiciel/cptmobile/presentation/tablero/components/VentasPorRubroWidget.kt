package ar.com.logiciel.cptmobile.presentation.tablero.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.formatearMoneda
import ar.com.logiciel.cptmobile.domain.model.tablero.VentaPorRubro
import ar.com.logiciel.cptmobile.presentation.common.DateFilterSheet

@Composable
fun VentasPorRubroWidget(
    ventasPorRubro: List<VentaPorRubro>,
    ventaMLPorRubro: List<VentaPorRubro>,
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
    
    val totalVentas = ventasPorRubro.sumOf { it.totalVentas ?: 0.0 }
    
    WidgetCard(
        title = "Venta por rubro",
        icon = Icons.Default.GridView,
        color = Color(0xFF9C27B0), // Purple
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
            StatRow(label = "Total Ventas", value = formatearMoneda(totalVentas))
            StatRow(label = "Categorías", value = ventasPorRubro.size.toString())
        },
        expandedContent = {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Gráfico") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Detalle") })
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            when (selectedTab) {
                0 -> {
                    Column {
                        if (ventasPorRubro.isNotEmpty()) {
                            Text(
                                text = "Completa",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            SimplePieChart(
                                data = ventasPorRubro.take(6).map { it.rubroNombre to (it.totalVentas ?: 0.0) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        
                        if (ventaMLPorRubro.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "MercadoLibre",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            SimplePieChart(
                                data = ventaMLPorRubro.map { it.rubroNombre to (it.totalVentas ?: 0.0) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                1 -> {
                    if (ventasPorRubro.isNotEmpty()) {
                        RubroTable(ventasPorRubro, totalVentas)
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
            title = "Filtro - Venta por rubro",
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
private fun RubroTable(ventasPorRubro: List<VentaPorRubro>, totalVentas: Double) {
    Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        // Header
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Rubro", fontWeight = FontWeight.Bold, modifier = Modifier.width(150.dp))
            Text("Facturado", fontWeight = FontWeight.Bold, modifier = Modifier.width(110.dp))
            Text("% margen", fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp))
            Text("% inc.", fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp))
        }
        
        HorizontalDivider()
        
        ventasPorRubro.forEach { rubro ->
            val incidencia = if (totalVentas > 0) ((rubro.totalVentas ?: 0.0) / totalVentas) * 100 else 0.0
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(rubro.rubroNombre, modifier = Modifier.width(150.dp))
                Text(formatearMoneda(rubro.totalVentas ?: 0.0), modifier = Modifier.width(110.dp))
                Text(rubro.margenTotalLogiciel ?: "0", modifier = Modifier.width(70.dp))
                Text(String.format("%.2f", incidencia), modifier = Modifier.width(60.dp))
            }
        }
        
        HorizontalDivider()
        
        // Total row
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Total:", fontWeight = FontWeight.Bold, modifier = Modifier.width(150.dp))
            Text(formatearMoneda(totalVentas), fontWeight = FontWeight.Bold, modifier = Modifier.width(110.dp))
            val avgMargen = ventasPorRubro.map { it.margenTotalLogiciel?.toDoubleOrNull() ?: 0.0 }.average()
            Text(String.format("%.0f", avgMargen), fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp))
        }
    }
}
