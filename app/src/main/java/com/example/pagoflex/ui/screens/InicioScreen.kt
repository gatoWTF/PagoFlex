package com.example.pagoflex.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pagoflex.R
import com.example.pagoflex.ui.components.FilaCompromiso
import com.example.pagoflex.ui.components.SnackbarPagoFlex
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.utils.FormatoMoneda
import com.example.pagoflex.utils.Notificaciones
import com.example.pagoflex.viewmodel.CompromisosViewModel
import kotlinx.coroutines.launch

// Pantalla de inicio del usuario final: su situacion del mes y la lista de compromisos (RF-02, RF-03).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    viewModel: CompromisosViewModel,
    alAbrirDetalle: (String) -> Unit,
    alAbrirPerfil: () -> Unit,
    alAbrirHistorial: () -> Unit,
    alAbrirConfiguracion: () -> Unit,
    alCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animacion 4: Snackbar para mensajes puntuales (ej. "Reporte enviado").
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.mensaje) {
        val texto = viewModel.mensaje
        if (texto != null) {
            snackbarHostState.showSnackbar(texto, duration = SnackbarDuration.Long)
            viewModel.consumirMensaje()
        }
    }

    // --- Notificaciones locales: aviso de vencimiento (RF-11, recurso nativo) ---
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { Notificaciones.crearCanal(context) }

    fun postearAviso() {
        val texto = viewModel.proximoVencimiento?.let {
            "Tu proximo pago vence el $it. Total por pagar: ${FormatoMoneda.clp(viewModel.totalPorPagar)}"
        } ?: "No tienes pagos pendientes."
        Notificaciones.mostrarAviso(context, "Aviso de vencimiento", texto)
        scope.launch { snackbarHostState.showSnackbar("Aviso de vencimiento enviado") }
    }

    val permisoNotif = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) postearAviso()
        else scope.launch { snackbarHostState.showSnackbar("Permiso de notificaciones denegado") }
    }

    // Desde Android 13 hay que pedir el permiso; antes no hace falta.
    fun avisarVencimiento() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val concedido = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (concedido) postearAviso() else permisoNotif.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            postearAviso()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) { datos -> SnackbarPagoFlex(datos) } },
        topBar = {
            TopAppBar(
                title = { Text("PagoFlex") },
                actions = {
                    IconButton(onClick = { avisarVencimiento() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_avisos),
                            contentDescription = "Avisarme el vencimiento"
                        )
                    }
                    IconButton(onClick = alAbrirPerfil) {
                        Icon(
                            painter = painterResource(R.drawable.ic_perfil),
                            contentDescription = "Mi perfil"
                        )
                    }
                    IconButton(onClick = alAbrirHistorial) {
                        Icon(
                            painter = painterResource(R.drawable.ic_historial),
                            contentDescription = "Historial de pagos"
                        )
                    }
                    IconButton(onClick = alAbrirConfiguracion) {
                        Icon(
                            painter = painterResource(R.drawable.ic_ajustes),
                            contentDescription = "Configuración"
                        )
                    }
                    IconButton(onClick = alCerrarSesion) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cerrar_sesion),
                            contentDescription = "Cerrar sesión"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(Dimens.espacioPantalla),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioChico)
        ) {
            item {
                ResumenSituacion(
                    total = viewModel.totalPorPagar,
                    proximoVencimiento = viewModel.proximoVencimiento,
                    vencidos = viewModel.cantidadVencidos
                )
            }
            item {
                Text(
                    text = "Tus compromisos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = Dimens.espacioChico)
                )
            }
            items(
                viewModel.compromisos.sortedBy { it.estado.prioridadLista },
                key = { it.folio }
            ) { compromiso ->
                FilaCompromiso(
                    compromiso = compromiso,
                    onClick = { alAbrirDetalle(compromiso.folio) },
                    modifier = Modifier.animateItem() // Animacion 2: aparicion/reordenamiento
                )
            }
        }
    }
}

// Tarjeta superior con el total por pagar, el proximo vencimiento y los vencidos (RF-02).
@Composable
private fun ResumenSituacion(
    total: Int,
    proximoVencimiento: String?,
    vencidos: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimens.radioTarjeta),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(modifier = Modifier.padding(Dimens.espacioGrande)) {
            Text(text = "Total por pagar este mes", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = FormatoMoneda.clp(total),
                style = MaterialTheme.typography.headlineMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.espacioMedio),
                horizontalArrangement = Arrangement.spacedBy(Dimens.espacioGrande)
            ) {
                Column {
                    Text(text = "Proximo vencimiento", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = proximoVencimiento ?: "Sin pagos pendientes",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Column {
                    Text(text = "Vencidos", style = MaterialTheme.typography.labelMedium)
                    Text(text = vencidos.toString(), style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}
