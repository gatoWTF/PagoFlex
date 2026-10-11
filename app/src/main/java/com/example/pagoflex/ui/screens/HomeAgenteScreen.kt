package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.ui.components.PantallaRolPlaceholder
import com.example.pagoflex.ui.theme.PagoFlexTheme

// Home del agente PagoFlex (R-02). Provisional: aqui ira la bandeja de reportes
// de problema para gestionarlos (RF-14, RF-15).
@Composable
fun HomeAgenteScreen(
    alCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    PantallaRolPlaceholder(
        titulo = "Agente PagoFlex",
        descripcion = "Aquí el agente gestionará los reportes de problema de los usuarios.",
        icono = R.drawable.ic_avisos,
        alCerrarSesion = alCerrarSesion,
        modifier = modifier
    )
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeAgenteScreenPreview() {
    PagoFlexTheme {
        HomeAgenteScreen(alCerrarSesion = {})
    }
}
