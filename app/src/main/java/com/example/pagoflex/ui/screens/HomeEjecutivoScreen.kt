package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.ui.components.PantallaRolPlaceholder
import com.example.pagoflex.ui.theme.PagoFlexTheme

// Home del ejecutivo de empresa cliente (R-03). Provisional: aqui ira el registro
// y anulacion de compromisos de su empresa y la cobranza del mes (RF-17 a RF-20).
@Composable
fun HomeEjecutivoScreen(
    alCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    PantallaRolPlaceholder(
        titulo = "Ejecutivo de empresa",
        descripcion = "Aquí el ejecutivo registrará y anulará compromisos de su empresa y verá su cobranza.",
        icono = R.drawable.ic_otro,
        alCerrarSesion = alCerrarSesion,
        modifier = modifier
    )
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeEjecutivoScreenPreview() {
    PagoFlexTheme {
        HomeEjecutivoScreen(alCerrarSesion = {})
    }
}
