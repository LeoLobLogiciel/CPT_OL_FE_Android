package ar.com.logiciel.cptmobile.presentation.ventas.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.com.logiciel.cptmobile.domain.model.*
import ar.com.logiciel.cptmobile.presentation.ventas.VentasUiState
import ar.com.logiciel.cptmobile.presentation.ventas.VentasViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasFiltrosSheet(
    uiState: VentasUiState,
    viewModel: VentasViewModel,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filtros de Ventas",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Botones (arriba de todo)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = onApply,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Aplicar")
                    }
                }
            }

            // Fechas
            item {
                FechasSection(
                    fechaDesde = uiState.fechaDesde,
                    fechaHasta = uiState.fechaHasta,
                    onFechaDesdeChange = viewModel::setFechaDesde,
                    onFechaHastaChange = viewModel::setFechaHasta
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Búsqueda
            item {
                BusquedaSection(
                    textoBusqueda = uiState.textoBusqueda,
                    onTextoBusquedaChange = viewModel::setTextoBusqueda
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Cliente
            item {
                ClienteSection(
                    clienteSeleccionado = uiState.clienteSeleccionado,
                    searchCliente = uiState.searchCliente,
                    clientesEncontrados = uiState.clientesEncontrados,
                    isLoadingClientes = uiState.isLoadingClientes,
                    onSearchClienteChange = viewModel::setSearchCliente,
                    onClienteSelect = viewModel::selectCliente,
                    onClienteClear = viewModel::clearCliente
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Artículo
            item {
                ArticuloSection(
                    articuloSeleccionado = uiState.articuloSeleccionado,
                    searchArticulo = uiState.searchArticulo,
                    articulosEncontrados = uiState.articulosEncontrados,
                    isLoadingArticulos = uiState.isLoadingArticulos,
                    onSearchArticuloChange = viewModel::setSearchArticulo,
                    onArticuloSelect = viewModel::selectArticulo,
                    onArticuloClear = viewModel::clearArticulo
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Rubros
            item {
                RubrosSection(
                    rubrosSeleccionados = uiState.rubrosSeleccionados,
                    rubrosFiltrados = uiState.rubrosFiltrados,
                    searchRubro = uiState.searchRubro,
                    isLoadingRubros = uiState.isLoadingRubros,
                    onSearchRubroChange = viewModel::setSearchRubro,
                    onRubroToggle = viewModel::toggleRubro
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Vendedor
            item {
                VendedorSection(
                    vendedorSeleccionado = uiState.vendedorSeleccionado,
                    vendedoresFiltrados = uiState.vendedoresFiltrados,
                    searchVendedor = uiState.searchVendedor,
                    isLoadingVendedores = uiState.isLoadingVendedores,
                    onSearchVendedorChange = viewModel::setSearchVendedor,
                    onVendedorSelect = viewModel::selectVendedor,
                    onVendedorClear = viewModel::clearVendedor
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Proveedor
            item {
                ProveedorSection(
                    proveedorSeleccionado = uiState.proveedorSeleccionado,
                    searchProveedor = uiState.searchProveedor,
                    proveedoresEncontrados = uiState.proveedoresEncontrados,
                    isLoadingProveedores = uiState.isLoadingProveedores,
                    onSearchProveedorChange = viewModel::setSearchProveedor,
                    onProveedorSelect = viewModel::selectProveedor,
                    onProveedorClear = viewModel::clearProveedor
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FechasSection(
    fechaDesde: LocalDate,
    fechaHasta: LocalDate,
    onFechaDesdeChange: (LocalDate) -> Unit,
    onFechaHastaChange: (LocalDate) -> Unit
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    var showDatePickerDesde by remember { mutableStateOf(false) }
    var showDatePickerHasta by remember { mutableStateOf(false) }

    Text(
        text = "Fechas",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))

    // Fecha Desde
    OutlinedTextField(
        value = fechaDesde.format(dateFormatter),
        onValueChange = {},
        label = { Text("Desde") },
        readOnly = true,
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePickerDesde = true },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Fecha Hasta
    OutlinedTextField(
        value = fechaHasta.format(dateFormatter),
        onValueChange = {},
        label = { Text("Hasta") },
        readOnly = true,
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePickerHasta = true },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )

    // DatePicker Dialogs
    if (showDatePickerDesde) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fechaDesde.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePickerDesde = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onFechaDesdeChange(newDate)
                        }
                        showDatePickerDesde = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDesde = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showDatePickerHasta) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fechaHasta.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePickerHasta = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onFechaHastaChange(newDate)
                        }
                        showDatePickerHasta = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerHasta = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun BusquedaSection(
    textoBusqueda: String,
    onTextoBusquedaChange: (String) -> Unit
) {
    Text(
        text = "Búsqueda",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = textoBusqueda,
        onValueChange = onTextoBusquedaChange,
        label = { Text("Buscar en ventas") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
private fun ClienteSection(
    clienteSeleccionado: Cliente?,
    searchCliente: String,
    clientesEncontrados: List<Cliente>,
    isLoadingClientes: Boolean,
    onSearchClienteChange: (String) -> Unit,
    onClienteSelect: (Cliente) -> Unit,
    onClienteClear: () -> Unit
) {
    Text(
        text = "Cliente",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))

    if (clienteSeleccionado != null) {
        // Mostrar cliente seleccionado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = clienteSeleccionado.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onClienteClear) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Quitar cliente",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    } else {
        // Búsqueda de cliente
        OutlinedTextField(
            value = searchCliente,
            onValueChange = onSearchClienteChange,
            label = { Text("Buscar cliente") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                if (isLoadingClientes) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }
            }
        )

        // Lista de clientes encontrados (máximo 5)
        clientesEncontrados.take(5).forEach { cliente ->
            TextButton(
                onClick = { onClienteSelect(cliente) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = cliente.nombre,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ArticuloSection(
    articuloSeleccionado: Articulo?,
    searchArticulo: String,
    articulosEncontrados: List<Articulo>,
    isLoadingArticulos: Boolean,
    onSearchArticuloChange: (String) -> Unit,
    onArticuloSelect: (Articulo) -> Unit,
    onArticuloClear: () -> Unit
) {
    Text(
        text = "Artículo",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))

    if (articuloSeleccionado != null) {
        // Mostrar artículo seleccionado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = articuloSeleccionado.nombre,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "SKU: ${articuloSeleccionado.sku}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
                IconButton(onClick = onArticuloClear) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Quitar artículo",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    } else {
        // Búsqueda de artículo
        OutlinedTextField(
            value = searchArticulo,
            onValueChange = onSearchArticuloChange,
            label = { Text("Buscar artículo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                if (isLoadingArticulos) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }
            }
        )

        // Lista de artículos encontrados (máximo 5)
        articulosEncontrados.take(5).forEach { articulo ->
            TextButton(
                onClick = { onArticuloSelect(articulo) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = articulo.nombre)
                    Text(
                        text = "SKU: ${articulo.sku}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun RubrosSection(
    rubrosSeleccionados: List<Rubro>,
    rubrosFiltrados: List<Rubro>,
    searchRubro: String,
    isLoadingRubros: Boolean,
    onSearchRubroChange: (String) -> Unit,
    onRubroToggle: (Rubro) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rubros",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (rubrosSeleccionados.isNotEmpty()) {
                    Text(
                        text = "(${rubrosSeleccionados.size})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // Campo de búsqueda
                OutlinedTextField(
                    value = searchRubro,
                    onValueChange = onSearchRubroChange,
                    label = { Text("Filtrar rubros") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        if (isLoadingRubros) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Lista de rubros con checkboxes
                rubrosFiltrados.forEach { rubro ->
                    val isSelected = rubrosSeleccionados.any { it.id == rubro.id }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRubroToggle(rubro) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { onRubroToggle(rubro) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = rubro.nombre)
                    }
                }
            }
        }
    }
}

@Composable
private fun VendedorSection(
    vendedorSeleccionado: Vendedor?,
    vendedoresFiltrados: List<Vendedor>,
    searchVendedor: String,
    isLoadingVendedores: Boolean,
    onSearchVendedorChange: (String) -> Unit,
    onVendedorSelect: (Vendedor) -> Unit,
    onVendedorClear: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Vendedor",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                vendedorSeleccionado?.let {
                    Text(
                        text = "(${it.nombre})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // Campo de búsqueda
                OutlinedTextField(
                    value = searchVendedor,
                    onValueChange = onSearchVendedorChange,
                    label = { Text("Filtrar vendedores") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        if (isLoadingVendedores) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Opción "(Ninguno)" para limpiar selección
                if (vendedorSeleccionado != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVendedorClear() }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = false,
                            onClick = { onVendedorClear() }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(Ninguno)",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Lista de vendedores con radio buttons
                vendedoresFiltrados.forEach { vendedor ->
                    val isSelected = vendedorSeleccionado?.id == vendedor.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVendedorSelect(vendedor) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onVendedorSelect(vendedor) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = vendedor.nombre)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProveedorSection(
    proveedorSeleccionado: Proveedor?,
    searchProveedor: String,
    proveedoresEncontrados: List<Proveedor>,
    isLoadingProveedores: Boolean,
    onSearchProveedorChange: (String) -> Unit,
    onProveedorSelect: (Proveedor) -> Unit,
    onProveedorClear: () -> Unit
) {
    Text(
        text = "Proveedor",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))

    if (proveedorSeleccionado != null) {
        // Mostrar proveedor seleccionado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = proveedorSeleccionado.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onProveedorClear) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Quitar proveedor",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    } else {
        // Búsqueda de proveedor
        OutlinedTextField(
            value = searchProveedor,
            onValueChange = onSearchProveedorChange,
            label = { Text("Buscar proveedor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                if (isLoadingProveedores) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }
            }
        )

        // Lista de proveedores encontrados (máximo 5)
        proveedoresEncontrados.take(5).forEach { proveedor ->
            TextButton(
                onClick = { onProveedorSelect(proveedor) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = proveedor.nombre,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
