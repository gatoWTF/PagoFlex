package com.example.pagoflex.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.ui.styles.EstilosBoton
import com.example.pagoflex.ui.styles.estiloAltoBoton
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme

@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    cargando: Boolean = false,
    @DrawableRes icono: Int? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.estiloAltoBoton(),
        enabled = habilitado && !cargando,
        shape = EstilosBoton.forma,
        colors = EstilosBoton.coloresPrincipal(),
        elevation = EstilosBoton.elevacion(),
        contentPadding = EstilosBoton.relleno
    ) {
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        } else if (icono != null) {
            Icon(
                painter = painterResource(id = icono),
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        }
        Text(text = texto)
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BotonPrincipalPreview() {
    PagoFlexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(Modifier.padding(Dimens.espacioMedio)) {
                BotonPrincipal(texto = "Iniciar sesión", onClick = {}, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.size(Dimens.espacioChico))
                BotonPrincipal(
                    texto = "Con ícono",
                    onClick = {},
                    icono = R.drawable.ic_ingresar,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.size(Dimens.espacioChico))
                BotonPrincipal(
                    texto = "Deshabilitado",
                    onClick = {},
                    habilitado = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
