package com.example.pagoflex.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagoflex.R
import com.example.pagoflex.model.EstadoCompromiso
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.Exito
import com.example.pagoflex.ui.theme.ExitoFondo
import com.example.pagoflex.ui.theme.GrisDeshabilitado
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.ui.theme.Pendiente
import com.example.pagoflex.ui.theme.PendienteFondo
import com.example.pagoflex.ui.theme.Vencido
import com.example.pagoflex.ui.theme.VencidoFondo

// Pildora de color con el estado del compromiso. Reutiliza los colores de estado del tema.
@Composable
fun EtiquetaEstado(estado: EstadoCompromiso, modifier: Modifier = Modifier) {
    val colorTexto: Color
    val colorFondo: Color
    val icono: Int
    when (estado) {
        EstadoCompromiso.PAGADO -> {
            colorTexto = Exito; colorFondo = ExitoFondo; icono = R.drawable.ic_pagado
        }
        EstadoCompromiso.PENDIENTE -> {
            colorTexto = Pendiente; colorFondo = PendienteFondo; icono = R.drawable.ic_pendiente
        }
        EstadoCompromiso.VENCIDO -> {
            colorTexto = Vencido; colorFondo = VencidoFondo; icono = R.drawable.ic_vencido
        }
        EstadoCompromiso.EN_REVISION -> {
            colorTexto = Pendiente; colorFondo = PendienteFondo; icono = R.drawable.ic_pendiente
        }
        EstadoCompromiso.ANULADO -> {
            colorTexto = GrisDeshabilitado; colorFondo = Color(0xFFEDECF2); icono = R.drawable.ic_otro
        }
    }

    Row(
        modifier = modifier
            .background(colorFondo, RoundedCornerShape(50))
            .padding(horizontal = Dimens.espacioChico, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,
            tint = colorTexto,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = estado.etiqueta,
            color = colorTexto,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EtiquetaEstadoPreview() {
    PagoFlexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(modifier = Modifier.padding(Dimens.espacioMedio)) {
                EtiquetaEstado(EstadoCompromiso.PAGADO)
                Spacer(Modifier.width(Dimens.espacioChico))
                EtiquetaEstado(EstadoCompromiso.VENCIDO)
            }
        }
    }
}
