package ar.com.logiciel.cptmobile.presentation.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ar.com.logiciel.cptmobile.presentation.pedidos.components.*
import timber.log.Timber

/**
 * Pantalla de Pedidos - Header igual a Ventas
 */
@Composable
fun PedidosScreen(
    modifier: Modifier = Modifier,
    viewModel: PedidosViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    Timber.d("📦 PedidosScreen COMPOSABLE EJECUTÁNDOSE")

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        Timber.d("📦 PedidosScreen INICIADO")
        viewModel.loadPedidos()
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Top Bar con botones (igual que Ventas)
        TopBarButtons(
            isLoading = uiState.isLoading,
            tieneFiltros = uiState.tieneFiltrosActivos,
            onFiltrosClick = { viewModel.showFiltrosSheet() },
            onActualizarClick = { viewModel.loadPedidos() },
            onColumnasClick = { viewModel.showColumnasSheet() }
        )

        // Contenido principal
        when {
            uiState.isLoading && uiState.pedidos.isEmpty() -> {
                LoadingState()
            }
            uiState.errorMessage != null && uiState.pedidos.isEmpty() -> {
                ErrorState(
                    errorMessage = uiState.errorMessage!!,
                    onRetry = { viewModel.loadPedidos() }
                )
            }
            uiState.pedidos.isEmpty() -> {
                EmptyState()
            }
            else -> {
                ContentState(
                    uiState = uiState
                )
            }
        }
    }

    // Bottom Sheet de Columnas
    if (uiState.showColumnasSheet) {
        PedidosColumnasSheet(
            columnasSeleccionadas = uiState.columnasSeleccionadas,
            onToggleColumna = { viewModel.toggleColumna(it) },
            onReset = { viewModel.resetColumnas() },
            onDismiss = { viewModel.hideColumnasSheet() }
        )
    }

    // Bottom Sheet de Filtros
    if (uiState.showFiltrosSheet) {
        PedidosFiltrosSheet(
            uiState = uiState,
            onFechaDesdeChange = { viewModel.setFechaDesde(it) },
            onFechaHastaChange = { viewModel.setFechaHasta(it) },
            onSearchChange = { viewModel.setSearch(it) },
            onSearchClienteChange = { viewModel.setSearchCliente(it) },
            onSelectCliente = { viewModel.selectCliente(it) },
            onClearCliente = { viewModel.clearCliente() },
            onSearchVendedorChange = { viewModel.setSearchVendedor(it) },
            onSelectVendedor = { viewModel.selectVendedor(it) },
            onSearchRubroChange = { viewModel.setSearchRubro(it) },
            onToggleRubro = { viewModel.toggleRubro(it) },
            onConfirmadosChange = { viewModel.setConfirmados(it) },
            onArmadosChange = { viewModel.setArmados(it) },
            onBackorderChange = { viewModel.setBackorder(it) },
            onMercadolibreChange = { viewModel.setMercadolibre(it) },
            onEsPrimeroChange = { viewModel.setEsPrimero(it) },
            onComercialChange = { viewModel.setComercial(it) },
            onCrediticioChange = { viewModel.setCrediticio(it) },
            onCerradosChange = { viewModel.setCerrados(it) },
            onFacturablesChange = { viewModel.setFacturables(it) },
            onReset = { viewModel.resetFiltros() },
            onApply = { viewModel.applyFiltros() },
            onDismiss = { viewModel.hideFiltrosSheet() }
        )
    }
}

@Composable
private fun TopBarButtons(
    isLoading: Boolean,
    tieneFiltros: Boolean,
    onFiltrosClick: () -> Unit,
    onActualizarClick: () -> Unit,
    onColumnasClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón Filtros con Badge
            BadgedBox(
                badge = {
                    if (tieneFiltros) {
                        Badge()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedButton(
                    onClick = onFiltrosClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Filtros")
                }
            }

            // Botón Columnas
            OutlinedButton(
                onClick = onColumnasClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.ViewColumn,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Columnas")
            }

            // Botón Actualizar
            Button(
                onClick = onActualizarClick,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Actualizar")
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
                text = "Cargando pedidos...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Inbox,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No se encontraron pedidos",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = "Ajusta los filtros para ver más resultados",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ErrorState(
    errorMessage: String,
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
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Error al cargar pedidos",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Button(onClick = onRetry) {
                Icon(Icons.Default.Refresh, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun ContentState(
    uiState: PedidosUiState
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Card de resumen
        PedidosResumenCard(
            totalPedidos = uiState.totalPedidos,
            totalConfirmados = uiState.totalConfirmados,
            totalBackorder = uiState.totalBackorder,
            totalMercadoLibre = uiState.totalMercadoLibre,
            montoTotal = uiState.montoTotal
        )

        // Lista de pedidos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = uiState.pedidos,
                key = { it.id }
            ) { pedido ->
                PedidoCard(
                    pedido = pedido,
                    columnas = uiState.columnasSeleccionadas
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
