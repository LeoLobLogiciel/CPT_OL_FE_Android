package ar.com.logiciel.cptmobile.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFilterSheet(
    title: String,
    fechaDesde: LocalDate,
    fechaHasta: LocalDate,
    onFechaDesdeChange: (LocalDate) -> Unit,
    onFechaHastaChange: (LocalDate) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    var showDesdePicker by remember { mutableStateOf(false) }
    var showHastaPicker by remember { mutableStateOf(false) }
    
    val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
    
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, "Cerrar")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Fecha Desde
            OutlinedTextField(
                value = fechaDesde.format(formatter),
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("Desde") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDesdePicker = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Fecha Hasta
            OutlinedTextField(
                value = fechaHasta.format(formatter),
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("Hasta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showHastaPicker = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onApply,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aplicar")
            }
        }
    }
    
    // DatePicker Desde
    if (showDesdePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fechaDesde.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDesdePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = java.time.Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            onFechaDesdeChange(date)
                        }
                        showDesdePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDesdePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    // DatePicker Hasta
    if (showHastaPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fechaHasta.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showHastaPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = java.time.Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            onFechaHastaChange(date)
                        }
                        showHastaPicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showHastaPicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// Helper para hacer clickable
private fun Modifier.clickable(onClick: () -> Unit): Modifier = this.then(
    androidx.compose.ui.Modifier.clickable(onClick = onClick)
)
