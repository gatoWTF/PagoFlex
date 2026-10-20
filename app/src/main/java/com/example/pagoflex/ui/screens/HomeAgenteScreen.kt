package com.example.pagoflex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pagoflex.R
import com.example.pagoflex.model.EstadoReporte
import com.example.pagoflex.ui.components.SnackbarPagoFlex
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.viewmodel.AgenteViewModel
import com.example.pagoflex.viewmodel.ReporteGestion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAgenteScreen(
    viewModel: AgenteViewModel,
    alAbrirPerfil: () -> Unit,
    alAbrirConfiguracion: () -> Unit,
    alCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.mensaje) {
        val texto = viewModel.mensaje
        if (texto != null) {
            snackbarHostState.showSnackbar(texto, duration = SnackbarDuration.Long)
            viewModel.consumirMensaje()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) { datos -> SnackbarPagoFlex(datos) } },
        topBar = {
            TopAppBar(
                title = { Text("Reportes") },
                actions = {
                    IconButton(onClick = alAbrirPerfil) {
                        Icon(painterResource(R.drawable.ic_perfil), contentDescription = "Mi perfil")
                    }
                    IconButton(onClick = alAbrirConfiguracion) {
                        Icon(painterResource(R.drawable.ic_ajustes), contentDescription = "Configuración")
                    }
                    IconButton(onClick = alCerrarSesion) {
                        Icon(painterResource(R.drawable.ic_cerrar_sesion), contentDescription = "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(Dimens.espacioPantalla),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioChico)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(Dimens.radioTarjeta),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Column(modifier = Modifier.padding(Dimens.espacioGrande)) {
                        Text("Bandeja de reportes", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "${viewModel.cantidadAbiertos} abiertos",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }

            if (viewModel.reportes.isEmpty()) {
                item {
                    Text(
                        text = "No hay reportes por ahora.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Dimens.espacioChico)
                    )
                }
            }

            items(viewModel.reportes, key = { it.numero }) { reporte ->
                FilaReporte(
                    reporte = reporte,
                    alTomar = { viewModel.tomar(reporte.numero) },
                    alResolver = { viewModel.resolver(reporte.numero) }
                )
            }
        }
    }
}

@Composable
private fun FilaReporte(
    reporte: ReporteGestion,
    alTomar: () -> Unit,
    alResolver: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.radioTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevacionTarjeta)
    ) {
        Column(modifier = Modifier.padding(Dimens.espacioMedio)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reporte.motivo,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                EstadoReporteChip(reporte.estado)
            }
            Text(
                text = reporte.usuario + " · " + reporte.fecha,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Dimens.espacioMini)
            )
            Text(
                text = reporte.comentario,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = Dimens.espacioMini)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.espacioMini),
                horizontalArrangement = Arrangement.End
            ) {
                when (reporte.estado) {
                    EstadoReporte.ABIERTO -> TextButton(onClick = alTomar) { Text("Tomar") }
                    EstadoReporte.EN_GESTION -> TextButton(onClick = alResolver) { Text("Resolver") }
                    EstadoReporte.RESUELTO -> {}
                }
            }
        }
    }
}

@Composable
private fun EstadoReporteChip(estado: EstadoReporte) {
    val fondo = when (estado) {
        EstadoReporte.ABIERTO -> MaterialTheme.colorScheme.errorContainer
        EstadoReporte.EN_GESTION -> MaterialTheme.colorScheme.tertiaryContainer
        EstadoReporte.RESUELTO -> MaterialTheme.colorScheme.secondaryContainer
    }
    val texto = when (estado) {
        EstadoReporte.ABIERTO -> MaterialTheme.colorScheme.onErrorContainer
        EstadoReporte.EN_GESTION -> MaterialTheme.colorScheme.onTertiaryContainer
        EstadoReporte.RESUELTO -> MaterialTheme.colorScheme.onSecondaryContainer
    }
    Row(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(estado.etiqueta, style = MaterialTheme.typography.labelMedium, color = texto)
    }
}
