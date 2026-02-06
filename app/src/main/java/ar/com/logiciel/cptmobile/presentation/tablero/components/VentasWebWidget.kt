package ar.com.logiciel.cptmobile.presentation.tablero.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.formatearMoneda
import ar.com.logiciel.cptmobile.domain.model.tablero.VentasWebEmpresas
import ar.com.logiciel.cptmobile.domain.model.tablero.VentasWebML
import ar.com.logiciel.cptmobile.presentation.common.DateFilterSheet

@Composable
fun VentasWebWidget(
    ventasWebML: List<VentasWebML>,
    ventasWebEmpresas: VentasWebEmpresas?,
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
    
    val totalVentasWebML = ventasWebML.sumOf { it.netoTotalValue }
    val totalCantidadWebML = ventasWebML.sumOf { it.cantidad }
    
    WidgetCard(
        title = "Ventas Web",
        icon = Icons.Default.ShoppingCart,
        color = Color.Blue,
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
            
            // MercadoLibre
            StatRow(
                label = "MercadoLibre",
                value = formatearMoneda(totalVentasWebML)
            )
            Text(
                text = "$totalCantidadWebML pedidos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Empresas
            ventasWebEmpresas?.let { empresas ->
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                StatRow(
                    label = "Web Empresas",
                    value = formatearMoneda(empresas.netoTotal)
                )
                Text(
                    text = "${empresas.cantidad} pedidos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        expandedContent = {
            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Gráfico") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Totales ML") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Empresas") }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            when (selectedTab) {
                0 -> {
                    // Gráfico de barras simplificado
                    if (ventasWebML.isNotEmpty()) {
                        SimpleBarChart(
                            data = ventasWebML.take(10).map { 
                                val fecha = it.fechaCreacion?.substring(5) ?: ""
                                val valor = it.netoTotalValue
                                fecha to valor
                            },
                            modifier = Modifier.height(200.dp)
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
                    // Tabla ML
                    Column {
                        ventasWebML.take(10).forEach { venta ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = venta.fechaCreacion ?: "N/A",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${venta.cantidad} pedidos",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = formatearMoneda(venta.netoTotalValue),
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(0.8f)
                                )
                            }
                        }
                    }
                }
                2 -> {
                    // Empresas
                    ventasWebEmpresas?.let { empresas ->
                        Column {
                            StatRow(label = "Cantidad", value = empresas.cantidad.toString())
                            StatRow(label = "Neto Total", value = formatearMoneda(empresas.netoTotal))
                        }
                    } ?: Text(
                        text = "No hay datos de empresas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
    
    if (showDateFilter) {
        DateFilterSheet(
            title = "Filtro - Ventas Web",
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
