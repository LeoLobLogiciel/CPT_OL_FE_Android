package ar.com.logiciel.cptmobile.presentation.tablero.components

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.formatearMoneda

@Composable
fun SimpleBarChart(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier,
    maxItems: Int = 0, // 0 = todos, >0 = limitar a N items con scroll
    barColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    if (data.isEmpty()) return
    
    val maxValue = data.maxOfOrNull { it.second } ?: 1.0
    
    val columnModifier = if (maxItems > 0 && data.size > maxItems) {
        modifier.verticalScroll(rememberScrollState())
    } else {
        modifier
    }
    
    Column(modifier = columnModifier) {
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
