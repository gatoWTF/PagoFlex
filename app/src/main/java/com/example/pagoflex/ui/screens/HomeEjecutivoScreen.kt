package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pagoflex.R
import com.example.pagoflex.model.CompromisoEmpresa
import com.example.pagoflex.ui.components.BotonPrincipal
import com.example.pagoflex.ui.components.EtiquetaEstado
import com.example.pagoflex.ui.components.SnackbarPagoFlex
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.utils.FormatoMoneda
import com.example.pagoflex.viewmodel.EjecutivoViewModel

// Home del ejecutivo de empresa cliente (R-03): cobranza del mes, lista de
// compromisos de su empresa y acciones de registrar/anular (RF-17 a RF-20).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeEjecutivoScreen(
    viewModel: EjecutivoViewModel,
    alRegistrar: () -> Unit,
    alCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Compromiso que se esta por anular; null = sin dialogo abierto.
    var compromisoAAnular by remember { mutableStateOf<CompromisoEmpresa?>(null) }

    // Animacion 4: Snackbar para "Compromiso registrado" / "Compromiso anulado".
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
                title = { Text(viewModel.empresa) },
                actions = {
                    IconButton(onClick = alCerrarSesion) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cerrar_sesion),
                            contentDescription = "Cerrar sesión"
                        )
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
                TarjetaCobranza(
                    tasa = viewModel.tasaCobranza,
                    recaudado = viewModel.totalRecaudado,
                    porCobrar = viewModel.totalPorCobrar
                )
            }
            item {
                BotonPrincipal(
                    texto = "Registrar compromiso",
                    onClick = alRegistrar,
                    icono = R.drawable.ic_ingresar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.espacioChico)
                )
            }
            item {
                Text(
                    text = "Compromisos de la empresa",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(
                viewModel.compromisos.sortedBy { it.estado.prioridadLista },
                key = { it.folio }
            ) { compromiso ->
                FilaCompromisoEmpresa(
                    compromiso = compromiso,
                    alAnular = { compromisoAAnular = compromiso },
                    modifier = Modifier.animateItem() // Animacion 2: al anular, la fila se reubica
                )
            }
        }
    }

    // Confirmacion antes de anular (accion no reversible).
    val porAnular = compromisoAAnular
    if (porAnular != null) {
        AlertDialog(
            onDismissRequest = { compromisoAAnular = null },
            title = { Text("Anular compromiso") },
            text = { Text("¿Seguro que quieres anular \"" + porAnular.concepto + "\" de " + porAnular.deudor + "? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.anular(porAnular.folio)
                    compromisoAAnular = null
                }) { Text("Anular") }
            },
            dismissButton = {
                TextButton(onClick = { compromisoAAnular = null }) { Text("Cancelar") }
            }
        )
    }
}

// Tarjeta superior con la tasa de cobranza y los montos del mes (RF-20, RN-24).
@Composable
private fun TarjetaCobranza(tasa: Int, recaudado: Int, porCobrar: Int) {
    // Animacion 6: la tasa sube de 0 al valor al entrar a la pantalla.
    var objetivo by remember { mutableStateOf(0) }
    LaunchedEffect(tasa) { objetivo = tasa }
    val tasaAnimada by animateIntAsState(
        targetValue = objetivo,
        animationSpec = tween(durationMillis = 900),
        label = "tasa"
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.radioTarjeta),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(modifier = Modifier.padding(Dimens.espacioGrande)) {
            Text(text = "Tasa de cobranza del mes", style = MaterialTheme.typography.bodyMedium)
            Text(text = "$tasaAnimada%", style = MaterialTheme.typography.headlineLarge)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.espacioChico),
                horizontalArrangement = Arrangement.spacedBy(Dimens.espacioGrande)
            ) {
                Column {
                    Text(text = "Recaudado", style = MaterialTheme.typography.labelMedium)
                    Text(text = FormatoMoneda.clp(recaudado), style = MaterialTheme.typography.titleSmall)
                }
                Column {
                    Text(text = "Por cobrar", style = MaterialTheme.typography.labelMedium)
                    Text(text = FormatoMoneda.clp(porCobrar), style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}

// Fila de un compromiso de la empresa, con la opcion de anular si corresponde.
@Composable
private fun FilaCompromisoEmpresa(
    compromiso: CompromisoEmpresa,
    alAnular: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.radioTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevacionTarjeta)
    ) {
        Column(modifier = Modifier.padding(Dimens.espacioMedio)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = compromiso.concepto,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = compromiso.deudor + " · " + compromiso.rutDeudor,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Vence " + compromiso.fechaVencimiento,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = FormatoMoneda.clp(compromiso.monto),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.espacioMini),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EtiquetaEstado(estado = compromiso.estado)
                if (compromiso.sePuedeAnular) {
                    TextButton(onClick = alAnular) { Text("Anular") }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeEjecutivoScreenPreview() {
    PagoFlexTheme {
        HomeEjecutivoScreen(
            viewModel = viewModel(),
            alRegistrar = {},
            alCerrarSesion = {}
        )
    }
}
