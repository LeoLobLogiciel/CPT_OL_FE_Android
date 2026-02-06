package ar.com.logiciel.cptmobile.presentation.tablero.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.formatearMoneda
import kotlin.math.min

@Composable
fun SimplePieChart(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier,
    showLegend: Boolean = true,
    colors: List<androidx.compose.ui.graphics.Color> = listOf(
        androidx.compose.ui.graphics.Color(0xFF2196F3), // Blue
        androidx.compose.ui.graphics.Color(0xFF4CAF50), // Green
        androidx.compose.ui.graphics.Color(0xFFFF9800), // Orange
        androidx.compose.ui.graphics.Color(0xFFF44336), // Red
        androidx.compose.ui.graphics.Color(0xFF9C27B0), // Purple
        androidx.compose.ui.graphics.Color(0xFF00BCD4), // Cyan
        androidx.compose.ui.graphics.Color(0xFFFFEB3B), // Yellow
        androidx.compose.ui.graphics.Color(0xFF795548), // Brown
    )
) {
    if (data.isEmpty()) return
    
    val total = data.sumOf { it.second }
    val validData = data.filter { it.second > 0 }
    
    if (validData.isEmpty()) {
        Text(
            text = "No hay datos para mostrar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }
    
    Column(modifier = modifier) {
        // Gráfico circular real
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(180.dp)
                    .padding(8.dp)
            ) {
                val canvasSize = min(size.width, size.height)
                val radius = canvasSize / 2
                val center = Offset(size.width / 2, size.height / 2)
                
                var startAngle = -90f // Empezar desde arriba
                
                validData.forEachIndexed { index, (_, value) ->
                    val sweepAngle = if (total > 0) {
                        (value / total * 360).toFloat()
                    } else 0f
                    
                    val color = colors[index % colors.size]
                    
                    // Dibujar sector
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(canvasSize, canvasSize)
                    )
                    
                    // Dibujar borde blanco para separar sectores
                    drawArc(
                        color = androidx.compose.ui.graphics.Color.White,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(canvasSize, canvasSize),
                        style = Stroke(width = 2f)
                    )
                    
                    startAngle += sweepAngle
                }
                
                // Círculo blanco en el centro para efecto donut
                drawCircle(
                    color = androidx.compose.ui.graphics.Color.White,
                    radius = radius * 0.4f,
                    center = center
                )
            }
            
            // Total en el centro
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatearMoneda(total),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Leyenda scrollable si hay muchos items
        if (showLegend) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                validData.forEachIndexed { index, (label, value) ->
                    val percentage = if (total > 0) (value / total * 100) else 0.0
                    val color = colors[index % colors.size]
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(color, RoundedCornerShape(4.dp))
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        
                        Text(
                            text = String.format("%.1f%%", percentage),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = formatearMoneda(value),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
