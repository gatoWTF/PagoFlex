package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.pagoflex.R
import com.example.pagoflex.ui.components.BotonPrincipal
import com.example.pagoflex.ui.components.CampoTexto
import com.example.pagoflex.ui.components.SnackbarPagoFlex
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.viewmodel.SesionViewModel

/**
 * Perfil del usuario (R-01). Muestra la identidad con la que se registro (rol,
 * nombre, RUT y correo) en solo lectura, y deja editar los datos de contacto:
 * un correo secundario de emergencia y un telefono.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    sesion: SesionViewModel,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(sesion.mensaje) {
        val texto = sesion.mensaje
        if (texto != null) {
            snackbarHostState.showSnackbar(texto, duration = SnackbarDuration.Short)
            sesion.consumirMensaje()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) { datos -> SnackbarPagoFlex(datos) } },
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.espacioPantalla),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)
        ) {
            // --- Identidad (solo lectura) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimens.radioTarjeta),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.espacioGrande),
                    verticalArrangement = Arrangement.spacedBy(Dimens.espacioChico)
                ) {
                    Text(
                        text = "Datos de tu cuenta",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Estos datos no se pueden editar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.size(Dimens.espacioMini))
                    FilaDato("Rol", sesion.rol?.etiqueta ?: "Sin rol")
                    FilaDato("Nombre", sesion.nombre.ifBlank { "Sin registrar" })
                    FilaDato("RUT", sesion.rut.ifBlank { "Sin registrar" })
                    FilaDato("Correo", sesion.correo.ifBlank { "Sin registrar" })
                }
            }

            // --- Contacto (editable) ---
            Text(
                text = "Datos de contacto",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Opcionales. Puedes dejar un correo de emergencia y un teléfono.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            CampoTexto(
                valor = sesion.correoSecundario,
                onValorCambia = sesion::cambiarCorreoSecundario,
                etiqueta = "Correo secundario (emergencia)",
                icono = R.drawable.ic_correo,
                error = sesion.errorCorreoSecundario,
                tipoTeclado = KeyboardType.Email
            )

            CampoTexto(
                valor = sesion.telefono,
                onValorCambia = sesion::cambiarTelefono,
                etiqueta = "Teléfono",
                icono = R.drawable.ic_telefono,
                error = sesion.errorTelefono,
                tipoTeclado = KeyboardType.Phone
            )

            Spacer(Modifier.size(Dimens.espacioMini))
            BotonPrincipal(
                texto = "Guardar cambios",
                onClick = sesion::guardarContacto,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Fila de un dato en solo lectura: etiqueta pequeña arriba, valor abajo.
@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.size(Dimens.espacioMini))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}
