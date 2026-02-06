package ar.com.logiciel.cptmobile.presentation.tablero

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ar.com.logiciel.cptmobile.presentation.tablero.components.*
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableroScreen(
    modifier: Modifier = Modifier,
    viewModel: TableroViewModel = hiltViewModel()
) {
    Timber.d("📊 TableroScreen iniciado")
    
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        if (uiState.ventasPorZona.isEmpty()) {
            viewModel.loadAllData()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tablero Principal") },
                actions = {
                    IconButton(onClick = { viewModel.loadAllData() }) {
                        Icon(Icons.Default.Refresh, "Actualizar")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading && uiState.ventasPorZona.isEmpty() -> {
                    LoadingState()
                }
                uiState.errorMessage != null && uiState.ventasPorZona.isEmpty() -> {
                    ErrorState(
                        message = uiState.errorMessage!!,
                        onRetry = { viewModel.loadAllData() }
                    )
                }
                else -> {
                    ContentState(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando tablero...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun ContentState(
    uiState: TableroUiState,
    viewModel: TableroViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Ventas Web Widget
        VentasWebWidget(
            ventasWebML = uiState.ventasWebML,
            ventasWebEmpresas = uiState.ventasWebEmpresas,
            fechaDesde = uiState.ventasWebFechaDesde,
            fechaHasta = uiState.ventasWebFechaHasta,
            isExpanded = uiState.ventasWebExpanded,
            onExpandedChange = { viewModel.setVentasWebExpanded(it) },
            onFechaDesdeChange = { viewModel.setVentasWebFechaDesde(it) },
            onFechaHastaChange = { viewModel.setVentasWebFechaHasta(it) },
            onRefresh = { viewModel.loadVentasWeb() }
        )
        
        // 2. Ventas por Zona Widget
        VentasPorZonaWidget(
            ventasPorZona = uiState.ventasPorZona,
            totalVentas = uiState.totalVentasPorZona,
            totalClientesNuevos = uiState.totalClientesNuevos,
            totalClientesAtendidos = uiState.totalClientesAtendidos,
            fechaDesde = uiState.ventasFechaDesde,
            fechaHasta = uiState.ventasFechaHasta,
            isExpanded = uiState.ventasZonaExpanded,
            onExpandedChange = { viewModel.setVentasZonaExpanded(it) },
            onFechaDesdeChange = { viewModel.setVentasFechaDesde(it) },
            onFechaHastaChange = { viewModel.setVentasFechaHasta(it) },
            onRefresh = { viewModel.loadVentas() }
        )
        
        // 3. Deuda Clientes Widget
        DeudaClientesWidget(
            deudaClientes = uiState.deudaClientes,
            totalDeuda = uiState.totalDeudaClientes,
            totalVencida = uiState.totalDeudaVencida,
            porcentajeVencida = uiState.porcentajeDeudaVencida,
            isExpanded = uiState.deudaClientesExpanded,
            onExpandedChange = { viewModel.setDeudaClientesExpanded(it) },
            onRefresh = { viewModel.loadDeudaClientes() }
        )
        
        // 4. Ventas por Rubro Widget
        VentasPorRubroWidget(
            ventasPorRubro = uiState.ventasPorRubro,
            ventaMLPorRubro = uiState.ventaMLPorRubro,
            fechaDesde = uiState.ventasRubroFechaDesde,
            fechaHasta = uiState.ventasRubroFechaHasta,
            isExpanded = uiState.ventasRubroExpanded,
            onExpandedChange = { viewModel.setVentasRubroExpanded(it) },
            onFechaDesdeChange = { viewModel.setVentasRubroFechaDesde(it) },
            onFechaHastaChange = { viewModel.setVentasRubroFechaHasta(it) },
            onRefresh = { viewModel.loadVentasRubro() }
        )
        
        // 5. Deuda Proveedores Widget
        DeudaProveedoresWidget(
            deudaProveedores = uiState.deudaProveedores,
            totalDeuda = uiState.totalDeudaProveedores,
            isExpanded = uiState.deudaProveedoresExpanded,
            onExpandedChange = { viewModel.setDeudaProveedoresExpanded(it) },
            onRefresh = { viewModel.loadDeudaProveedores() }
        )
        
        // 6. Resmas Widget
        ResmasWidget(
            resmas = uiState.resmas,
            totalStock = uiState.totalStockResmas,
            totalComprometido = uiState.totalComprometidoResmas,
            totalDisponible = uiState.totalDisponibleResmas,
            isExpanded = uiState.resmasExpanded,
            onExpandedChange = { viewModel.setResmasExpanded(it) },
            onRefresh = { viewModel.loadResmas() }
        )
        
        // Espacio al final
        Spacer(modifier = Modifier.height(16.dp))
    }
}
