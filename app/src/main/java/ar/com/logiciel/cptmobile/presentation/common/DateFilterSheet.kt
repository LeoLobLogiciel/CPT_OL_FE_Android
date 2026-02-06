package ar.com.logiciel.cptmobile.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        DateFilterSheetContent(
            title = title,
            fechaDesde = fechaDesde,
            fechaHasta = fechaHasta,
            onFechaDesdeChange = onFechaDesdeChange,
            onFechaHastaChange = onFechaHastaChange,
            onApply = {
                onApply()
            },
            onDismiss = onDismiss
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateFilterSheetContent(
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
        
        // Fecha Desde - usando Button en lugar de OutlinedTextField con clickable
        OutlinedButton(
            onClick = { showDesdePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Desde", style = MaterialTheme.typography.labelSmall)
                Text(fechaDesde.format(formatter), style = MaterialTheme.typography.bodyLarge)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Fecha Hasta
        OutlinedButton(
            onClick = { showHastaPicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Hasta", style = MaterialTheme.typography.labelSmall)
                Text(fechaHasta.format(formatter), style = MaterialTheme.typography.bodyLarge)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onApply,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Aplicar")
        }
    }
    
    // DatePicker Desde - usando una capa superior cuando se muestra
    if (showDesdePicker) {
        DatePickerPopup(
            initialDate = fechaDesde,
            onDateSelected = { date ->
                date?.let { onFechaDesdeChange(it) }
                showDesdePicker = false
            },
            onDismiss = { showDesdePicker = false }
        )
    }
    
    // DatePicker Hasta
    if (showHastaPicker) {
        DatePickerPopup(
            initialDate = fechaHasta,
            onDateSelected = { date ->
                date?.let { onFechaHastaChange(it) }
                showHastaPicker = false
            },
            onDismiss = { showHastaPicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerPopup(
    initialDate: LocalDate,
    onDateSelected: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val date = datePickerState.selectedDateMillis?.let { millis ->
                        java.time.Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                    }
                    onDateSelected(date)
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
