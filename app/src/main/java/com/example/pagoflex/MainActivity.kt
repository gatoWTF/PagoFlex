package com.example.pagoflex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pagoflex.ui.navigation.Rutas
import com.example.pagoflex.ui.screens.ComprobanteScreen
import com.example.pagoflex.ui.screens.DetalleCompromisoScreen
import com.example.pagoflex.ui.screens.HistorialScreen
import com.example.pagoflex.ui.screens.HomeAgenteScreen
import com.example.pagoflex.ui.screens.HomeEjecutivoScreen
import com.example.pagoflex.ui.screens.InicioScreen
import com.example.pagoflex.ui.screens.RegistrarCompromisoScreen
import com.example.pagoflex.ui.screens.ReportarProblemaScreen
import com.example.pagoflex.ui.screens.SelectorRolScreen
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.viewmodel.CompromisosViewModel
import com.example.pagoflex.viewmodel.EjecutivoViewModel

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
    // VMs con alcance de Activity: las pantallas de cada rol comparten su estado.
    val compromisosViewModel: CompromisosViewModel = viewModel()
    val ejecutivoViewModel: EjecutivoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Rutas.SelectorRol.ruta,
        // Transicion al navegar: la pantalla nueva entra desde la derecha; al volver, al reves.
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(450)) + fadeIn(tween(450))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(450)) + fadeOut(tween(450))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(450)) + fadeIn(tween(450))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(450)) + fadeOut(tween(450))
        }
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
                alAbrirHistorial = { navController.navigate(Rutas.Historial.ruta) },
                alCerrarSesion = { irAlSelector(navController) }
            )
        }

        // Historial de pagos (RF-07, RF-08)
        composable(Rutas.Historial.ruta) {
            HistorialScreen(
                viewModel = compromisosViewModel,
                alVolver = { navController.popBackStack() }
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
                    val comprobante = compromisosViewModel.pagar(folioPagado)
                    // Deja Inicio en la pila y muestra el comprobante encima (RF-06).
                    navController.navigate(Rutas.Comprobante.crear(comprobante.folio)) {
                        popUpTo(Rutas.InicioUsuario.ruta)
                    }
                },
                alReportar = { folioReporte ->
                    navController.navigate(Rutas.ReportarProblema.crear(folioReporte))
                }
            )
        }

        // Comprobante del pago (RF-05, RF-06)
        composable(
            route = Rutas.Comprobante.ruta,
            arguments = listOf(
                navArgument(Rutas.Comprobante.ARG_FOLIO) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val folioComprobante = backStackEntry.arguments?.getString(Rutas.Comprobante.ARG_FOLIO)
            ComprobanteScreen(
                comprobante = folioComprobante?.let { compromisosViewModel.buscarComprobante(it) },
                alFinalizar = { navController.popBackStack() }
            )
        }

        // Reportar un problema sobre un compromiso (RF-09)
        composable(
            route = Rutas.ReportarProblema.ruta,
            arguments = listOf(
                navArgument(Rutas.ReportarProblema.ARG_FOLIO) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val folio = backStackEntry.arguments?.getString(Rutas.ReportarProblema.ARG_FOLIO)
            ReportarProblemaScreen(
                compromiso = folio?.let { compromisosViewModel.buscarPorFolio(it) },
                alVolver = { navController.popBackStack() },
                alEnviar = {
                    if (folio != null) compromisosViewModel.reportarProblema(folio)
                    navController.popBackStack(Rutas.InicioUsuario.ruta, inclusive = false)
                }
            )
        }

        // Home del agente (R-02)
        composable(Rutas.HomeAgente.ruta) {
            HomeAgenteScreen(alCerrarSesion = { irAlSelector(navController) })
        }

        // Home del ejecutivo (R-03)
        composable(Rutas.HomeEjecutivo.ruta) {
            HomeEjecutivoScreen(
                viewModel = ejecutivoViewModel,
                alRegistrar = { navController.navigate(Rutas.RegistrarCompromiso.ruta) },
                alCerrarSesion = { irAlSelector(navController) }
            )
        }

        // Registrar un compromiso (RF-17)
        composable(Rutas.RegistrarCompromiso.ruta) {
            RegistrarCompromisoScreen(
                alVolver = { navController.popBackStack() },
                onRegistrar = { deudor, rut, concepto, monto, fecha ->
                    ejecutivoViewModel.registrarCompromiso(deudor, rut, concepto, monto, fecha)
                    navController.popBackStack()
                }
            )
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
