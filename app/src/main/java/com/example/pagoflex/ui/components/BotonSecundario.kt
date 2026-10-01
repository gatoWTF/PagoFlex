package com.example.pagoflex.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.ui.styles.EstilosBoton
import com.example.pagoflex.ui.styles.estiloAltoBoton
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme

/** Acción de menor importancia (Cancelar, Cerrar sesión): solo borde, sin relleno. */
@Composable
fun BotonSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.estiloAltoBoton(),
        shape = EstilosBoton.forma,
        colors = EstilosBoton.coloresSecundario(),
        border = EstilosBoton.bordeSecundario(),        // border: 2px solid
        contentPadding = EstilosBoton.relleno
    ) {
        Text(text = texto)
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BotonSecundarioPreview() {
    PagoFlexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            BotonSecundario(
                texto = "Cerrar sesión",
                onClick = {},
                modifier = Modifier.padding(Dimens.espacioMedio)
            )
        }
    }
}
