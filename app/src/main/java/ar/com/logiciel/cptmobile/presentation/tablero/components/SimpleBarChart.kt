package ar.com.logiciel.cptmobile.presentation.tablero.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.formatearMoneda
import kotlin.math.max

@Composable
fun SimpleBarChart(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier,
    barColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    if (data.isEmpty()) return
    
    val maxValue = data.maxOfOrNull { it.second } ?: 1.0
    
    Column(modifier = modifier) {
        data.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(50.dp)
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(24.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    val percentage = if (maxValue > 0) (value / maxValue).toFloat() else 0f
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(percentage)
                            .clip(RoundedCornerShape(4.dp))
                            .background(barColor)
                    )
                }
                
                Text(
                    text = formatearMoneda(value),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(80.dp)
                )
            }
        }
    }
}

@Composable
fun SimplePieChart(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier,
    colors: List<androidx.compose.ui.graphics.Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.surfaceVariant
    )
) {
    if (data.isEmpty()) return
    
    val total = data.sumOf { it.second }
    
    Column(modifier = modifier) {
        data.forEachIndexed { index, (label, value) ->
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
                    style = MaterialTheme.typography.bodySmall
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
