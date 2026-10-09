package com.example.pagoflex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pagoflex.ui.navigation.Rutas
import com.example.pagoflex.ui.screens.DetalleCompromisoScreen
import com.example.pagoflex.ui.screens.HomeAgenteScreen
import com.example.pagoflex.ui.screens.HomeEjecutivoScreen
import com.example.pagoflex.ui.screens.InicioScreen
import com.example.pagoflex.ui.screens.SelectorRolScreen
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

// Navegacion de la app con NavHost. El flujo arranca en el selector de rol
// (sesion simulada, RF-01) y cada rol entra a su propio home (RNF-13, RN-22).
@Composable
private fun AppPagoFlex() {
    val navController = rememberNavController()
    // VM con alcance de Activity: Inicio y Detalle comparten el mismo estado de compromisos.
    val compromisosViewModel: CompromisosViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Rutas.SelectorRol.ruta
    ) {
        // Selector de rol (entrada)
        composable(Rutas.SelectorRol.ruta) {
            SelectorRolScreen(
                alElegirRol = { rol ->
                    navController.navigate(Rutas.homeDeRol(rol).ruta)
                }
            )
        }

        // Home del usuario final (R-01)
        composable(Rutas.InicioUsuario.ruta) {
            InicioScreen(
                viewModel = compromisosViewModel,
                alAbrirDetalle = { folio ->
                    navController.navigate(Rutas.DetalleCompromiso.crear(folio))
                },
                alCerrarSesion = { irAlSelector(navController) }
            )
        }

        // Detalle de un compromiso (RF-04), recibe el folio por argumento
        composable(
            route = Rutas.DetalleCompromiso.ruta,
            arguments = listOf(
                navArgument(Rutas.DetalleCompromiso.ARG_FOLIO) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val folio = backStackEntry.arguments?.getString(Rutas.DetalleCompromiso.ARG_FOLIO)
            DetalleCompromisoScreen(
                compromiso = folio?.let { compromisosViewModel.buscarPorFolio(it) },
                alVolver = { navController.popBackStack() },
                alPagar = { folioPagado ->
                    compromisosViewModel.pagar(folioPagado)
                    navController.popBackStack()
                }
            )
        }

        // Home del agente (R-02)
        composable(Rutas.HomeAgente.ruta) {
            HomeAgenteScreen(alCerrarSesion = { irAlSelector(navController) })
        }

        // Home del ejecutivo (R-03)
        composable(Rutas.HomeEjecutivo.ruta) {
            HomeEjecutivoScreen(alCerrarSesion = { irAlSelector(navController) })
        }
    }
}

// Cierra la sesion simulada: vuelve al selector y limpia el historial de navegacion.
private fun irAlSelector(navController: NavHostController) {
    navController.navigate(Rutas.SelectorRol.ruta) {
        popUpTo(Rutas.SelectorRol.ruta) { inclusive = true }
        launchSingleTop = true
    }
}
