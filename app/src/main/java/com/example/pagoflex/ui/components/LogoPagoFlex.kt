package com.example.pagoflex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pagoflex.ui.theme.Acento
import com.example.pagoflex.ui.theme.Pendiente
import com.example.pagoflex.ui.theme.PagoFlexTheme

/**
 * Logo de la marca: la letra P sobre un disco claro y la moneda ambar, igual
 * que el icono de la app. Es un componente reutilizable (se usa en el
 * encabezado del login, del registro y donde haga falta).
 * @param tamano diametro del disco; el resto escala en proporcion.
 */
@Composable
fun LogoPagoFlex(
    modifier: Modifier = Modifier,
    tamano: Dp = 60.dp
) {
    Box(
        modifier = modifier
            .size(tamano)
            .background(MaterialTheme.colorScheme.onPrimary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "P",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = (tamano.value * 0.55f).sp
        )
        // La moneda: disco ambar con un anillo cafe, abajo a la derecha.
        val moneda = tamano * 0.3f
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = -(tamano * 0.14f), y = -(tamano * 0.16f))
                .size(moneda)
                .background(Acento, CircleShape)
                .border(1.dp, Pendiente, CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LogoPagoFlexPreview() {
    PagoFlexTheme {
        Surface(color = MaterialTheme.colorScheme.primary) {
            LogoPagoFlex(modifier = Modifier.size(80.dp).offset(16.dp, 16.dp))
        }
    }
}
