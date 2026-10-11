package com.example.pagoflex.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme

/**
 * Encabezado de marca: banda morada con el logo, el nombre y un subtitulo.
 * Transmite que la persona esta en el canal oficial de PagoFlex (RNF-05).
 * @param compacto true usa una fila mas baja (para pantallas con muchos campos,
 *                 como el registro); false usa una columna centrada mas alta.
 */
@Composable
fun EncabezadoMarca(
    titulo: String,
    subtitulo: String,
    modifier: Modifier = Modifier,
    compacto: Boolean = false
) {
    val forma = RoundedCornerShape(
        bottomStart = Dimens.radioTarjeta,
        bottomEnd = Dimens.radioTarjeta
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, forma)
            .padding(horizontal = Dimens.espacioGrande, vertical = if (compacto) Dimens.espacioGrande else Dimens.espacioPantalla),
        horizontalArrangement = if (compacto) Arrangement.Start else Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (compacto) {
            LogoPagoFlex(tamano = 48.dp)
            Spacer(Modifier.size(Dimens.espacioMedio))
            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LogoPagoFlex(tamano = 64.dp)
                Spacer(Modifier.size(Dimens.espacioChico))
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EncabezadoMarcaPreview() {
    PagoFlexTheme {
        Column {
            EncabezadoMarca(
                titulo = "PagoFlex",
                subtitulo = "Tu canal oficial de pagos"
            )
            Spacer(Modifier.size(Dimens.espacioMedio))
            EncabezadoMarca(
                titulo = "PagoFlex",
                subtitulo = "Crea tu cuenta en el canal oficial",
                compacto = true
            )
        }
    }
}
