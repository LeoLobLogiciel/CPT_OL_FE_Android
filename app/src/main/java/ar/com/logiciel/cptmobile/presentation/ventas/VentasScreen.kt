package ar.com.logiciel.cptmobile.presentation.ventas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ar.com.logiciel.cptmobile.domain.model.TipoAgrupacion
import ar.com.logiciel.cptmobile.presentation.ventas.components.*
import timber.log.Timber

/**
 * Pantalla de Ventas - Implementación completa basada en iOS
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(
    viewModel: VentasViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    // LOG INMEDIATO para ver si llega aquí
    Timber.d("🛒🛒🛒 VentasScreen COMPOSABLE EJECUTÁNDOSE")

    val uiState by viewModel.uiState.collectAsState()

    // LOGS para debug
    LaunchedEffect(Unit) {
        Timber.d("🛒 VentasScreen INICIADO - LaunchedEffect")
        // Cargar ventas al inicio (igual que iOS con .task)
        viewModel.loadVentas()
    }

    LaunchedEffect(uiState) {
        Timber.d("🛒 VentasScreen - Estado actual:")
        Timber.d("   - isLoading: ${uiState.isLoading}")
        Timber.d("   - errorMessage: ${uiState.errorMessage}")
        Timber.d("   - ventas.size: ${uiState.ventas.size}")
        Timber.d("   - tipoAgrupacion: ${uiState.tipoAgrupacion}")
        Timber.d("   - resumen: ${uiState.resumen}")
    }

    // Mostrar bottom sheet de filtros si está activo
    if (uiState.showFiltrosSheet) {
        VentasFiltrosSheet(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { viewModel.hideFiltros() },
            onApply = { viewModel.applyFiltros() }
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top Bar con botones
        TopBarButtons(
            isLoading = uiState.isLoading,
            onFiltrosClick = { viewModel.showFiltros() },
            onActualizarClick = { viewModel.loadVentas() }
        )

        // Contenido principal
        when {
            uiState.isLoading -> {
                // Estado de carga
                LoadingState()
            }
            uiState.errorMessage != null -> {
                // Estado de error
                ErrorState(
                    errorMessage = uiState.errorMessage!!,
                    onRetry = { viewModel.loadVentas() }
                )
            }
            uiState.ventas.isEmpty() -> {
                // Estado vacío
                EmptyState()
            }
            else -> {
                // Estado con datos
                ContentState(
                    uiState = uiState,
                    onTipoAgrupacionChange = { viewModel.setTipoAgrupacion(it) }
                )
            }
        }
    }
}

@Composable
private fun TopBarButtons(
    isLoading: Boolean,
    onFiltrosClick: () -> Unit,
    onActualizarClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Botón Filtros
        OutlinedButton(
            onClick = onFiltrosClick,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Filtros")
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
                text = "Cargando...",
                style = MaterialTheme.typography.bodyLarge
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
            Text(
                text = errorMessage,
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
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay ventas para mostrar",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun ContentState(
    uiState: VentasUiState,
    onTipoAgrupacionChange: (TipoAgrupacion) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Card de resumen
        item {
            uiState.resumen?.let { resumen ->
                VentasResumenCard(
                    resumen = resumen,
                    tipoAgrupacion = uiState.tipoAgrupacion,
                    onTipoAgrupacionChange = onTipoAgrupacionChange
                )
            }
        }

        // Lista según modo de vista
        if (uiState.tipoAgrupacion == TipoAgrupacion.DETALLE) {
            // Mostrar detalle de ventas
            items(uiState.ventas) { venta ->
                VentaDetalleItem(venta = venta)
            }
        } else {
            // Mostrar datos agrupados
            items(uiState.ventasAgrupadas) { ventaAgrupada ->
                VentaAgrupadaItem(ventaAgrupada = ventaAgrupada)
            }
        }
    }
}
