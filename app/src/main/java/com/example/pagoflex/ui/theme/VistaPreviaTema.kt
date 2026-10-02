package com.example.pagoflex.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R

// Solo para mirar el tema en Android Studio (vista Split o Design). La app no lo usa.

@Composable
private fun CuadroDeColor(nombre: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(Dimens.tamanoIconoCategoria)
                .background(color, RoundedCornerShape(Dimens.radioCampo))
        )
        Text(text = nombre, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun EstadoDeEjemplo(texto: String, @DrawableRes icono: Int, color: Color, fondo: Color) {
    Row(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(50))
            .padding(horizontal = Dimens.espacioMedio, vertical = Dimens.espacioChico),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(Dimens.tamanoIcono)
        )
        Spacer(Modifier.width(Dimens.espacioChico))
        Text(text = texto, color = color, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun MuestraDelTema() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(Dimens.espacioPantalla),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)
        ) {
            Text(
                text = "PagoFlex",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Texto normal sobre el fondo",
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.espacioChico)) {
                Button(onClick = {}) { Text("Principal") }
                OutlinedButton(onClick = {}) { Text("Secundario") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.espacioChico)) {
                CuadroDeColor("Primario", Primario)
                CuadroDeColor("Oscuro", PrimarioOscuro)
                CuadroDeColor("Claro", PrimarioClaro)
                CuadroDeColor("Secund.", Secundario)
                CuadroDeColor("Acento", Acento)
            }
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.espacioChico)) {
                EstadoDeEjemplo("PAGADO", R.drawable.ic_pagado, Exito, ExitoFondo)
                EstadoDeEjemplo("PENDIENTE", R.drawable.ic_pendiente, Pendiente, PendienteFondo)
                EstadoDeEjemplo("VENCIDO", R.drawable.ic_vencido, Vencido, VencidoFondo)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)) {
                val iconos = listOf(
                    R.drawable.ic_luz, R.drawable.ic_agua, R.drawable.ic_gas,
                    R.drawable.ic_internet, R.drawable.ic_telefono, R.drawable.ic_otro
                )
                iconos.forEach { icono ->
                    Icon(
                        painter = painterResource(icono),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Tema claro")
@Composable
private fun TemaClaroPreview() {
    PagoFlexTheme(darkTheme = false) { MuestraDelTema() }
}

@Preview(showBackground = true, name = "Tema oscuro")
@Composable
private fun TemaOscuroPreview() {
    PagoFlexTheme(darkTheme = true) { MuestraDelTema() }
}