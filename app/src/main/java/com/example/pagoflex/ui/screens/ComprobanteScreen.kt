package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagoflex.R
import com.example.pagoflex.model.Comprobante
import com.example.pagoflex.ui.components.BotonPrincipal
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.Exito
import com.example.pagoflex.ui.theme.ExitoFondo
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.utils.FormatoMoneda

// Comprobante de pago (RF-05, RF-06): confirma el pago y muestra sus datos.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComprobanteScreen(
    comprobante: Comprobante?,
    alFinalizar: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animacion 3: el circulo de exito entra con efecto resorte al mostrar el comprobante.
    val escala = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        escala.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Comprobante") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (comprobante == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontro el comprobante.")
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
                    .size(72.dp)
                    .scale(escala.value)
                    .background(ExitoFondo, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_pagado),
                    contentDescription = null,
                    tint = Exito,
                    modifier = Modifier.size(Dimens.tamanoIconoCategoria)
                )
            }
            Text(
                text = "¡Pago realizado!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Guarda este comprobante como respaldo de tu pago.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.size(Dimens.espacioChico))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimens.radioTarjeta)
            ) {
                Column(modifier = Modifier.padding(Dimens.espacioMedio)) {
                    FilaDato("Concepto", comprobante.concepto)
                    FilaDato("Empresa", comprobante.empresa)
                    HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.espacioChico))
                    FilaDato("Monto pagado", FormatoMoneda.clp(comprobante.monto))
                    FilaDato("Fecha y hora", comprobante.fechaHora)
                    FilaDato("Canal", comprobante.canal)
                    FilaDato("N° comprobante", comprobante.folio)
                }
            }

            Spacer(Modifier.weight(1f))

            BotonPrincipal(
                texto = "Listo",
                onClick = alFinalizar,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.espacioMini),
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
private fun ComprobanteScreenPreview() {
    PagoFlexTheme {
        ComprobanteScreen(
            comprobante = Comprobante(
                folio = "CPR-000460",
                folioCompromiso = "CP-2026-0102",
                concepto = "Cuota social octubre",
                empresa = "Club Los Halcones",
                monto = 17000,
                fechaHora = "09-10-2026 14:32",
                aTiempo = false
            ),
            alFinalizar = {}
        )
    }
}
