package com.example.pagoflex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pagoflex.model.ModoTema
import com.example.pagoflex.ui.navigation.Rutas
import com.example.pagoflex.ui.screens.ComprobanteScreen
import com.example.pagoflex.ui.screens.ConfiguracionScreen
import com.example.pagoflex.ui.screens.DetalleCompromisoScreen
import com.example.pagoflex.ui.screens.HistorialScreen
import com.example.pagoflex.ui.screens.HomeAgenteScreen
import com.example.pagoflex.ui.screens.HomeEjecutivoScreen
import com.example.pagoflex.ui.screens.InicioScreen
import com.example.pagoflex.ui.screens.LoginScreen
import com.example.pagoflex.ui.screens.PerfilScreen
import com.example.pagoflex.ui.screens.RegistrarCompromisoScreen
import com.example.pagoflex.ui.screens.RegistroScreen
import com.example.pagoflex.ui.screens.ReportarProblemaScreen
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.viewmodel.AgenteViewModel
import com.example.pagoflex.viewmodel.CompromisosViewModel
import com.example.pagoflex.viewmodel.ConfiguracionViewModel
import com.example.pagoflex.viewmodel.EjecutivoViewModel
import com.example.pagoflex.viewmodel.LoginViewModel
import com.example.pagoflex.viewmodel.RegistroViewModel
import com.example.pagoflex.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val configuracionViewModel: ConfiguracionViewModel = viewModel()
            val oscuro = when (configuracionViewModel.modoTema) {
                ModoTema.CLARO -> false
                ModoTema.OSCURO -> true
                ModoTema.SISTEMA -> isSystemInDarkTheme()
            }
            PagoFlexTheme(darkTheme = oscuro) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppPagoFlex(configuracionViewModel)
                }
            }
        }
    }
}

@Composable
private fun AppPagoFlex(configuracionViewModel: ConfiguracionViewModel) {
    val navController = rememberNavController()

    val compromisosViewModel: CompromisosViewModel = viewModel()
    val ejecutivoViewModel: EjecutivoViewModel = viewModel()
    val agenteViewModel: AgenteViewModel = viewModel()

    val sesionViewModel: SesionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Rutas.Login.ruta,

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

        composable(Rutas.Login.ruta) {
            val loginViewModel: LoginViewModel = viewModel()
            LaunchedEffect(loginViewModel.rol) {
                loginViewModel.rol?.let { rol ->
                    sesionViewModel.iniciarDesdeLogin(loginViewModel.correo, rol)
                    navController.navigate(Rutas.homeDeRol(rol).ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                    loginViewModel.consumirRol()
                }
            }
            LoginScreen(
                correo = loginViewModel.correo,
                contrasena = loginViewModel.contrasena,
                contrasenaVisible = loginViewModel.contrasenaVisible,
                onCorreoCambia = loginViewModel::cambiarCorreo,
                onContrasenaCambia = loginViewModel::cambiarContrasena,
                onAlternarVisible = loginViewModel::alternarContrasenaVisible,
                onIngresar = loginViewModel::iniciarSesion,
                onIrARegistro = { navController.navigate(Rutas.Registro.ruta) },
                errorCorreo = loginViewModel.errorCorreo,
                errorContrasena = loginViewModel.errorContrasena
            )
        }

        composable(Rutas.Registro.ruta) {
            val registroViewModel: RegistroViewModel = viewModel()
            LaunchedEffect(registroViewModel.rol) {
                registroViewModel.rol?.let { rol ->
                    sesionViewModel.iniciarDesdeRegistro(
                        registroViewModel.nombre,
                        registroViewModel.rut,
                        registroViewModel.correo,
                        rol
                    )
                    navController.navigate(Rutas.homeDeRol(rol).ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                    registroViewModel.consumirRol()
                }
            }
            RegistroScreen(
                nombre = registroViewModel.nombre,
                rut = registroViewModel.rut,
                correo = registroViewModel.correo,
                contrasena = registroViewModel.contrasena,
                confirmar = registroViewModel.confirmar,
                contrasenaVisible = registroViewModel.contrasenaVisible,
                onNombreCambia = registroViewModel::cambiarNombre,
                onRutCambia = registroViewModel::cambiarRut,
                onCorreoCambia = registroViewModel::cambiarCorreo,
                onContrasenaCambia = registroViewModel::cambiarContrasena,
                onConfirmarCambia = registroViewModel::cambiarConfirmar,
                onAlternarVisible = registroViewModel::alternarContrasenaVisible,
                onCrearCuenta = registroViewModel::crearCuenta,
                onIrALogin = { navController.popBackStack() },
                onRutPerderFoco = registroViewModel::validarRut,
                onCorreoPerderFoco = registroViewModel::validarCorreo,
                errorNombre = registroViewModel.errorNombre,
                errorRut = registroViewModel.errorRut,
                errorCorreo = registroViewModel.errorCorreo,
                errorContrasena = registroViewModel.errorContrasena,
                errorConfirmar = registroViewModel.errorConfirmar
            )
        }

        composable(Rutas.InicioUsuario.ruta) {
            InicioScreen(
                viewModel = compromisosViewModel,
                alAbrirDetalle = { folio ->
                    navController.navigate(Rutas.DetalleCompromiso.crear(folio))
                },
                alAbrirPerfil = { navController.navigate(Rutas.Perfil.ruta) },
                alAbrirHistorial = { navController.navigate(Rutas.Historial.ruta) },
                alAbrirConfiguracion = { navController.navigate(Rutas.Configuracion.ruta) },
                alCerrarSesion = { irALogin(navController) }
            )
        }

        composable(Rutas.Perfil.ruta) {
            PerfilScreen(
                sesion = sesionViewModel,
                alVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.Historial.ruta) {
            HistorialScreen(
                viewModel = compromisosViewModel,
                alVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.Configuracion.ruta) {
            ConfiguracionScreen(
                viewModel = configuracionViewModel,
                alVolver = { navController.popBackStack() }
            )
        }

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

                    navController.navigate(Rutas.Comprobante.crear(comprobante.folio)) {
                        popUpTo(Rutas.InicioUsuario.ruta)
                    }
                },
                alReportar = { folioReporte ->
                    navController.navigate(Rutas.ReportarProblema.crear(folioReporte))
                }
            )
        }

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
                alEnviar = { motivo, descripcion ->
                    if (folio != null) compromisosViewModel.reportarProblema(folio, motivo, descripcion)
                    navController.popBackStack(Rutas.InicioUsuario.ruta, inclusive = false)
                }
            )
        }

        composable(Rutas.HomeAgente.ruta) {
            HomeAgenteScreen(
                viewModel = agenteViewModel,
                alAbrirPerfil = { navController.navigate(Rutas.Perfil.ruta) },
                alAbrirConfiguracion = { navController.navigate(Rutas.Configuracion.ruta) },
                alCerrarSesion = { irALogin(navController) }
            )
        }

        composable(Rutas.HomeEjecutivo.ruta) {
            HomeEjecutivoScreen(
                viewModel = ejecutivoViewModel,
                alRegistrar = { navController.navigate(Rutas.RegistrarCompromiso.ruta) },
                alAbrirPerfil = { navController.navigate(Rutas.Perfil.ruta) },
                alAbrirConfiguracion = { navController.navigate(Rutas.Configuracion.ruta) },
                alCerrarSesion = { irALogin(navController) }
            )
        }

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

private fun irALogin(navController: NavHostController) {
    navController.navigate(Rutas.Login.ruta) {
        popUpTo(Rutas.Login.ruta) { inclusive = true }
        launchSingleTop = true
    }
}
