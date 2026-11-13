package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import com.example.pagoflex.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.pagoflex.ui.components.BotonPrincipal
import com.example.pagoflex.ui.components.CampoContrasena
import com.example.pagoflex.ui.components.SnackbarPagoFlex
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.viewmodel.CambiarContrasenaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CambiarContrasenaScreen(
    viewModel: CambiarContrasenaViewModel,
    idUsuario: Long?,
    alVolver: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.mensaje) {
        val texto = viewModel.mensaje
        if (texto != null) {
            snackbarHostState.showSnackbar(texto)
            viewModel.consumirMensaje()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) { datos -> SnackbarPagoFlex(datos) } },
        topBar = {
            TopAppBar(
                title = { Text("Cambiar contraseña") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(
                            painter = painterResource(R.drawable.ic_volver),
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(Dimens.espacioPantalla),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)
        ) {
            CampoContrasena(
                valor = viewModel.actual,
                onValorCambia = viewModel::cambiarActual,
                etiqueta = "Contraseña actual",
                visible = viewModel.visible,
                onAlternarVisible = viewModel::alternarVisible,
                error = viewModel.errorActual
            )
            CampoContrasena(
                valor = viewModel.nueva,
                onValorCambia = viewModel::cambiarNueva,
                etiqueta = "Nueva contraseña",
                visible = viewModel.visible,
                onAlternarVisible = viewModel::alternarVisible,
                error = viewModel.errorNueva
            )
            CampoContrasena(
                valor = viewModel.confirmar,
                onValorCambia = viewModel::cambiarConfirmar,
                etiqueta = "Repite la nueva contraseña",
                visible = viewModel.visible,
                onAlternarVisible = viewModel::alternarVisible,
                error = viewModel.errorConfirmar
            )
            val error = viewModel.errorGeneral
            if (error != null) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }
            BotonPrincipal(
                texto = "Guardar contraseña",
                onClick = { viewModel.guardar(idUsuario) },
                cargando = viewModel.cargando,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}