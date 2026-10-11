package com.example.pagoflex.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.ui.theme.Pendiente
import com.example.pagoflex.ui.theme.PendienteFondo

/**
 * Nota informativa de fondo ambar: un aviso breve y de tono positivo (RNF-04).
 * Reutilizable; en el registro explica que el rol se asigna segun el correo.
 */
@Composable
fun NotaInfo(
    texto: String,
    modifier: Modifier = Modifier,
    @DrawableRes icono: Int = R.drawable.ic_avisos
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(PendienteFondo, RoundedCornerShape(Dimens.radioCampo))
            .padding(Dimens.espacioMedio),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,
            tint = Pendiente,
            modifier = Modifier.size(Dimens.tamanoIcono)
        )
        Spacer(Modifier.size(Dimens.espacioChico))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall,
            color = Pendiente
        )
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NotaInfoPreview() {
    PagoFlexTheme {
        NotaInfo(
            texto = "Tu rol se asigna segun el dominio de tu correo.",
            modifier = Modifier.padding(Dimens.espacioMedio)
        )
    }
}
