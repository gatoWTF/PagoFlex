package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pagoflex.R
import com.example.pagoflex.model.Comprobante
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.Exito
import com.example.pagoflex.ui.theme.ExitoFondo
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.ui.theme.Vencido
import com.example.pagoflex.ui.theme.VencidoFondo
import com.example.pagoflex.utils.FormatoMoneda
import com.example.pagoflex.viewmodel.CompromisosViewModel

// Historial de pagos (RF-07) con el indicador de cumplimiento (RF-08).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    viewModel: CompromisosViewModel,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Historial de pagos") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(
                            painter = painterResource(R.drawable.ic_volver),
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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
                TarjetaCumplimiento(
                    porcentaje = viewModel.porcentajeCumplimiento,
                    aTiempo = viewModel.comprobantes.count { it.aTiempo },
                    total = viewModel.comprobantes.size
                )
            }
            item {
                Text(
                    text = "Tus pagos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = Dimens.espacioChico)
                )
            }
            items(viewModel.comprobantes, key = { it.folio }) { comprobante ->
                FilaComprobante(comprobante, modifier = Modifier.animateItem())
            }
        }
    }
}

// Indicador de cumplimiento: porcentaje de pagos a tiempo (RF-08, RN-10).
@Composable
private fun TarjetaCumplimiento(porcentaje: Int, aTiempo: Int, total: Int) {
    // Animacion 6: el porcentaje sube de 0 al valor al entrar a la pantalla.
    var objetivo by remember { mutableStateOf(0) }
    LaunchedEffect(porcentaje) { objetivo = porcentaje }
    val porcentajeAnimado by animateIntAsState(
        targetValue = objetivo,
        animationSpec = tween(durationMillis = 900),
        label = "cumplimiento"
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
            Text(text = "Cumplimiento de pagos", style = MaterialTheme.typography.bodyMedium)
            Text(text = "$porcentajeAnimado%", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "$aTiempo de $total pagos a tiempo",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// Fila de un pago del historial.
@Composable
private fun FilaComprobante(comprobante: Comprobante, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.radioTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevacionTarjeta)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.espacioMedio),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = comprobante.concepto,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = comprobante.empresa + " · " + comprobante.fechaHora,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                PildoraCumplimiento(aTiempo = comprobante.aTiempo)
            }
            Text(
                text = FormatoMoneda.clp(comprobante.monto),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Pildora que indica si el pago fue a tiempo o atrasado.
@Composable
private fun PildoraCumplimiento(aTiempo: Boolean) {
    val texto = if (aTiempo) "A tiempo" else "Atrasado"
    val colorTexto = if (aTiempo) Exito else Vencido
    val colorFondo = if (aTiempo) ExitoFondo else VencidoFondo
    Text(
        text = texto,
        color = colorTexto,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .padding(top = Dimens.espacioMini)
            .background(colorFondo, RoundedCornerShape(50))
            .padding(horizontal = Dimens.espacioChico, vertical = 2.dp)
    )
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HistorialScreenPreview() {
    PagoFlexTheme {
        HistorialScreen(viewModel = viewModel(), alVolver = {})
    }
}
