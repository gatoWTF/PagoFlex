package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagoflex.R
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.EstadoCompromiso
import com.example.pagoflex.ui.components.BotonPrincipal
import com.example.pagoflex.ui.components.BotonSecundario
import com.example.pagoflex.ui.components.EtiquetaEstado
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.utils.FormatoMoneda
import kotlinx.coroutines.delay

// Detalle de un compromiso (RF-04): monto, recargo, total y accion de pago simulado (RF-05).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCompromisoScreen(
    compromiso: Compromiso?,
    alVolver: () -> Unit,
    alPagar: (String) -> Unit,
    alReportar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Animacion 8: al pagar, el boton muestra un spinner un instante antes de confirmar.
    var pagando by remember { mutableStateOf(false) }
    LaunchedEffect(pagando) {
        if (pagando && compromiso != null) {
            delay(1100)
            alPagar(compromiso.folio)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Detalle del pago") },
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
        if (compromiso == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontro el compromiso.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.espacioPantalla),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioChico)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_otro),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(Dimens.tamanoIconoCategoria)
                )
            }
            Text(
                text = compromiso.concepto,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = compromiso.empresa,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            EtiquetaEstado(estado = compromiso.estado)

            Spacer(Modifier.size(Dimens.espacioChico))

            FilaDato("Monto", FormatoMoneda.clp(compromiso.monto))
            if (compromiso.recargo > 0) {
                FilaDato("Recargo por atraso", FormatoMoneda.clp(compromiso.recargo))
            }
            FilaDato("Total a pagar", FormatoMoneda.clp(compromiso.totalAPagar))
            HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.espacioChico))
            FilaDato("Vence", compromiso.fechaVencimiento)
            compromiso.cuota?.let { FilaDato("Cuota", it) }
            FilaDato("Folio", compromiso.folio)

            Spacer(Modifier.weight(1f))

            if (compromiso.sePuedePagar) {
                BotonPrincipal(
                    texto = if (pagando) "Procesando…" else "Pagar " + FormatoMoneda.clp(compromiso.totalAPagar),
                    onClick = { pagando = true },
                    cargando = pagando,
                    icono = R.drawable.ic_pagar,
                    modifier = Modifier.fillMaxWidth()
                )
                if (!pagando) {
                    Spacer(Modifier.size(Dimens.espacioMini))
                    BotonSecundario(
                        texto = "Reportar un problema",
                        onClick = { alReportar(compromiso.folio) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetalleCompromisoScreenPreview() {
    PagoFlexTheme {
        DetalleCompromisoScreen(
            compromiso = Compromiso(
                folio = "CP-2026-0102",
                empresa = "Club Los Halcones",
                rubro = "Club deportivo",
                concepto = "Cuota social octubre",
                cuota = "10/12",
                monto = 15000,
                fechaVencimiento = "05-10-2026",
                estado = EstadoCompromiso.VENCIDO,
                recargo = 2000
            ),
            alVolver = {},
            alPagar = {},
            alReportar = {}
        )
    }
}
