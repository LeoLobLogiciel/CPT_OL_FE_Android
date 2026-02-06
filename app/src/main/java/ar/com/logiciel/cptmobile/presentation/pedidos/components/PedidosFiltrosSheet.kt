package ar.com.logiciel.cptmobile.presentation.pedidos.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.*
import ar.com.logiciel.cptmobile.presentation.pedidos.PedidosUiState
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosFiltrosSheet(
    uiState: PedidosUiState,
    onFechaDesdeChange: (LocalDate) -> Unit,
    onFechaHastaChange: (LocalDate) -> Unit,
    onSearchChange: (String) -> Unit,
    onSearchClienteChange: (String) -> Unit,
    onSelectCliente: (Cliente) -> Unit,
    onClearCliente: () -> Unit,
    onSearchVendedorChange: (String) -> Unit,
    onSelectVendedor: (Vendedor?) -> Unit,
    onSearchRubroChange: (String) -> Unit,
    onToggleRubro: (Rubro) -> Unit,
    onConfirmadosChange: (FiltroConfirmados) -> Unit,
    onArmadosChange: (FiltroArmados) -> Unit,
    onBackorderChange: (FiltroBackorder) -> Unit,
    onMercadolibreChange: (FiltroMercadoLibre) -> Unit,
    onEsPrimeroChange: (FiltroEsPrimero) -> Unit,
    onComercialChange: (FiltroAprobacionComercial) -> Unit,
    onCrediticioChange: (FiltroAprobacionCrediticia) -> Unit,
    onCerradosChange: (FiltroCerrados) -> Unit,
    onFacturablesChange: (FiltroFacturables) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtros",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Row {
                    TextButton(onClick = onReset) {
                        Text("Limpiar")
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            ) {
                // Fechas
                item {
                    FiltroSectionTitle("Fechas *")
                    FechasSection(
                        fechaDesde = uiState.fechaDesde,
                        fechaHasta = uiState.fechaHasta,
                        onFechaDesdeChange = onFechaDesdeChange,
                        onFechaHastaChange = onFechaHastaChange
                    )
                }
                
                // Cliente
                item {
                    FiltroSectionTitle("Cliente")
                    ClienteSection(
                        search = uiState.searchCliente,
                        clientes = uiState.clientesEncontrados,
                        isLoading = uiState.isLoadingClientes,
                        searchActual = uiState.search,
                        onSearchChange = onSearchClienteChange,
                        onSelect = onSelectCliente,
                        onClear = onClearCliente
                    )
                }
                
                // Vendedor - Colapsable
                item {
                    VendedorSectionColapsable(
                        vendedores = uiState.vendedoresFiltrados,
                        search = uiState.searchVendedor,
                        selectedId = uiState.idVendedor,
                        onSearchChange = onSearchVendedorChange,
                        onSelect = onSelectVendedor
                    )
                }
                
                // Rubros - Colapsable
                item {
                    RubrosSectionColapsable(
                        rubros = uiState.rubrosFiltrados,
                        selectedIds = uiState.idArticulosRubros,
                        search = uiState.searchRubro,
                        onSearchChange = onSearchRubroChange,
                        onToggle = onToggleRubro
                    )
                }
                
                // Estados
                item {
                    FiltroSectionTitle("Estados")
                    EstadosSection(
                        confirmados = uiState.confirmados,
                        armados = uiState.armados,
                        backorder = uiState.backorder,
                        mercadolibre = uiState.mercadolibre,
                        esPrimero = uiState.esPrimero,
                        comercial = uiState.comercial,
                        crediticio = uiState.crediticio,
                        cerrados = uiState.cerrados,
                        facturables = uiState.facturables,
                        onConfirmadosChange = onConfirmadosChange,
                        onArmadosChange = onArmadosChange,
                        onBackorderChange = onBackorderChange,
                        onMercadolibreChange = onMercadolibreChange,
                        onEsPrimeroChange = onEsPrimeroChange,
                        onComercialChange = onComercialChange,
                        onCrediticioChange = onCrediticioChange,
                        onCerradosChange = onCerradosChange,
                        onFacturablesChange = onFacturablesChange
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            // Botón Aplicar
            Button(
                onClick = onApply,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aplicar Filtros")
            }
        }
    }
}

@Composable
private fun FiltroSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FechasSection(
    fechaDesde: LocalDate,
    fechaHasta: LocalDate,
    onFechaDesdeChange: (LocalDate) -> Unit,
    onFechaHastaChange: (LocalDate) -> Unit
) {
    var showDesdePicker by remember { mutableStateOf(false) }
    var showHastaPicker by remember { mutableStateOf(false) }
    
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Fecha Desde
        OutlinedTextField(
            value = fechaDesde.format(formatter),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("Desde *") },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                .weight(1f)
                .clickable { showDesdePicker = true }
        )
        
        // Fecha Hasta
        OutlinedTextField(
            value = fechaHasta.format(formatter),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("Hasta *") },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                .weight(1f)
                .clickable { showHastaPicker = true }
        )
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

@Composable
private fun ClienteSection(
    search: String,
    clientes: List<Cliente>,
    isLoading: Boolean,
    searchActual: String,
    onSearchChange: (String) -> Unit,
    onSelect: (Cliente) -> Unit,
    onClear: () -> Unit
) {
    if (searchActual.isNotEmpty()) {
        ListItem(
            headlineContent = { Text(searchActual) },
            leadingContent = {
                Icon(Icons.Default.Person, contentDescription = null)
            },
            trailingContent = {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, "Quitar")
                }
            }
        )
    } else {
        OutlinedTextField(
            value = search,
            onValueChange = onSearchChange,
            label = { Text("Buscar cliente...") },
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Search, "Buscar")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        if (clientes.isNotEmpty()) {
            Column {
                clientes.take(5).forEach { cliente ->
                    ListItem(
                        headlineContent = { Text(cliente.nombre) },
                        leadingContent = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        modifier = Modifier.clickable { onSelect(cliente) }
                    )
                }
            }
        }
    }
}

@Composable
private fun VendedorSectionColapsable(
    vendedores: List<Vendedor>,
    search: String,
    selectedId: Int?,
    onSearchChange: (String) -> Unit,
    onSelect: (Vendedor?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedNombre = vendedores.find { it.id == selectedId }?.nombre ?: "Todos"
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Vendedor",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = selectedNombre,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Colapsar" else "Expandir"
                )
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = search,
                    onValueChange = onSearchChange,
                    label = { Text("Buscar...") },
                    trailingIcon = {
                        Icon(Icons.Default.Search, "Buscar")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                val vendedoresFiltrados = if (search.isEmpty()) {
                    vendedores
                } else {
                    vendedores.filter { it.nombre.contains(search, ignoreCase = true) }
                }
                
                Column {
                    ListItem(
                        headlineContent = { Text("Todos los vendedores") },
                        leadingContent = {
                            RadioButton(
                                selected = selectedId == null,
                                onClick = { onSelect(null) }
                            )
                        },
                        modifier = Modifier.clickable { onSelect(null) }
                    )
                    
                    vendedoresFiltrados.take(8).forEach { vendedor ->
                        ListItem(
                            headlineContent = { Text(vendedor.nombre) },
                            leadingContent = {
                                RadioButton(
                                    selected = selectedId == vendedor.id,
                                    onClick = { onSelect(vendedor) }
                                )
                            },
                            modifier = Modifier.clickable { onSelect(vendedor) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RubrosSectionColapsable(
    rubros: List<Rubro>,
    selectedIds: List<Int>,
    search: String,
    onSearchChange: (String) -> Unit,
    onToggle: (Rubro) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = if (selectedIds.isEmpty()) {
        "Todos"
    } else {
        "${selectedIds.size} seleccionado(s)"
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Rubros",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = selectedText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Colapsar" else "Expandir"
                )
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = search,
                    onValueChange = onSearchChange,
                    label = { Text("Buscar...") },
                    trailingIcon = {
                        Icon(Icons.Default.Search, "Buscar")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                val rubrosFiltrados = if (search.isEmpty()) {
                    rubros
                } else {
                    rubros.filter { it.nombre.contains(search, ignoreCase = true) }
                }
                
                Column {
                    rubrosFiltrados.take(10).forEach { rubro ->
                        val isSelected = selectedIds.contains(rubro.id)
                        ListItem(
                            headlineContent = { Text(rubro.nombre) },
                            leadingContent = {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { onToggle(rubro) }
                                )
                            },
                            modifier = Modifier.clickable { onToggle(rubro) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EstadosSection(
    confirmados: FiltroConfirmados,
    armados: FiltroArmados,
    backorder: FiltroBackorder,
    mercadolibre: FiltroMercadoLibre,
    esPrimero: FiltroEsPrimero,
    comercial: FiltroAprobacionComercial,
    crediticio: FiltroAprobacionCrediticia,
    cerrados: FiltroCerrados,
    facturables: FiltroFacturables,
    onConfirmadosChange: (FiltroConfirmados) -> Unit,
    onArmadosChange: (FiltroArmados) -> Unit,
    onBackorderChange: (FiltroBackorder) -> Unit,
    onMercadolibreChange: (FiltroMercadoLibre) -> Unit,
    onEsPrimeroChange: (FiltroEsPrimero) -> Unit,
    onComercialChange: (FiltroAprobacionComercial) -> Unit,
    onCrediticioChange: (FiltroAprobacionCrediticia) -> Unit,
    onCerradosChange: (FiltroCerrados) -> Unit,
    onFacturablesChange: (FiltroFacturables) -> Unit
) {
    FiltroTriState("Confirmados", confirmados, onConfirmadosChange)
    FiltroTriState("Armados", armados, onArmadosChange)
    FiltroTriState("Backorder", backorder, onBackorderChange)
    FiltroTriState("Mercado Libre", mercadolibre, onMercadolibreChange)
    FiltroTriState("Es Primero", esPrimero, onEsPrimeroChange)
    FiltroTriState("Aprobación Comercial", comercial, onComercialChange)
    FiltroTriState("Aprobación Crediticia", crediticio, onCrediticioChange)
    FiltroTriState("Cerrados", cerrados, onCerradosChange)
    FiltroTriState("Facturables", facturables, onFacturablesChange)
}

@Composable
private fun <T> FiltroTriState(
    label: String,
    seleccionado: T,
    onChange: (T) -> Unit
) where T : Enum<T> {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            val opciones = seleccionado.javaClass.enumConstants
            opciones?.forEachIndexed { index, opcion ->
                SegmentedButton(
                    selected = seleccionado == opcion,
                    onClick = { onChange(opcion) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = opciones.size
                    )
                ) {
                    val labelText = when (opcion) {
                        is FiltroConfirmados -> opcion.label
                        is FiltroArmados -> opcion.label
                        is FiltroBackorder -> opcion.label
                        is FiltroMercadoLibre -> opcion.label
                        is FiltroEsPrimero -> opcion.label
                        is FiltroAprobacionComercial -> opcion.label
                        is FiltroAprobacionCrediticia -> opcion.label
                        is FiltroCerrados -> opcion.label
                        is FiltroFacturables -> opcion.label
                        else -> opcion.name
                    }
                    Text(labelText)
                }
            }
        }
    }
}
