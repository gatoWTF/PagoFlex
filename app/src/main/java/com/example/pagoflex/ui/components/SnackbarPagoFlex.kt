package com.example.pagoflex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.Exito

// Snackbar con el estilo de PagoFlex: pildora redondeada con un punto de color,
// en vez de la barra negra plana por defecto de Material.
@Composable
fun SnackbarPagoFlex(data: SnackbarData) {
    Snackbar(
        modifier = Modifier.padding(Dimens.espacioChico),
        shape = RoundedCornerShape(Dimens.radioTarjeta),
        containerColor = MaterialTheme.colorScheme.inverseSurface,
        contentColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(
                modifier = Modifier
                    .size(9.dp)
                    .background(Exito, CircleShape)
            )
            Spacer(Modifier.width(Dimens.espacioChico))
            Text(text = data.visuals.message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
