package com.example.pagoflex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pagoflex.ui.screens.DetalleCompromisoScreen
import com.example.pagoflex.ui.screens.InicioScreen
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.viewmodel.CompromisosViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PagoFlexTheme {
                AppPagoFlex()
            }
        }
    }
}

// Navegacion simple por estado: Inicio y Detalle. Mas adelante se puede migrar a NavHost.
@Composable
private fun AppPagoFlex() {
    val viewModel: CompromisosViewModel = viewModel()
    // folio del compromiso abierto; null = estamos en Inicio
    var folioAbierto by rememberSaveable { mutableStateOf<String?>(null) }

    val folio = folioAbierto
    if (folio == null) {
        InicioScreen(
            viewModel = viewModel,
            alAbrirDetalle = { folioAbierto = it }
        )
    } else {
        DetalleCompromisoScreen(
            compromiso = viewModel.buscarPorFolio(folio),
            alVolver = { folioAbierto = null },
            alPagar = { folioPagado ->
                viewModel.pagar(folioPagado)
                folioAbierto = null
            }
        )
        // El boton atras del telefono tambien vuelve a Inicio
        BackHandler { folioAbierto = null }
    }
}
