package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.ui.components.BotonPrincipal
import com.example.pagoflex.ui.components.CampoContrasena
import com.example.pagoflex.ui.components.CampoTexto
import com.example.pagoflex.ui.components.EncabezadoMarca
import com.example.pagoflex.ui.components.NotaInfo
import com.example.pagoflex.ui.components.PieConfianza
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme

/**
 * Pantalla de registro (RF-01, sesion simulada). Pide nombre, RUT, correo,
 * contrasena y su confirmacion. El rol no se elige: se deduce del correo, por
 * eso la nota ambar lo explica. Es "sin estado": la logica va en el ViewModel.
 */
@Composable
fun RegistroScreen(
    nombre: String,
    rut: String,
    correo: String,
    contrasena: String,
    confirmar: String,
    contrasenaVisible: Boolean,
    onNombreCambia: (String) -> Unit,
    onRutCambia: (String) -> Unit,
    onCorreoCambia: (String) -> Unit,
    onContrasenaCambia: (String) -> Unit,
    onConfirmarCambia: (String) -> Unit,
    onAlternarVisible: () -> Unit,
    onCrearCuenta: () -> Unit,
    onIrALogin: () -> Unit,
    modifier: Modifier = Modifier,
    onRutPerderFoco: (() -> Unit)? = null,
    onCorreoPerderFoco: (() -> Unit)? = null,
    errorNombre: String? = null,
    errorRut: String? = null,
    errorCorreo: String? = null,
    errorContrasena: String? = null,
    errorConfirmar: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        EncabezadoMarca(
            titulo = stringResource(R.string.titulo_registro),
            subtitulo = stringResource(R.string.marca_subtitulo_registro),
            compacto = true
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.espacioPantalla),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)
        ) {
            CampoTexto(
                valor = nombre,
                onValorCambia = onNombreCambia,
                etiqueta = stringResource(R.string.label_nombre),
                icono = R.drawable.ic_perfil,
                error = errorNombre
            )

            CampoTexto(
                valor = rut,
                onValorCambia = onRutCambia,
                etiqueta = stringResource(R.string.label_rut),
                icono = R.drawable.ic_otro,
                error = errorRut,
                onPerderFoco = onRutPerderFoco
            )

            CampoTexto(
                valor = correo,
                onValorCambia = onCorreoCambia,
                etiqueta = stringResource(R.string.label_correo),
                icono = R.drawable.ic_correo,
                error = errorCorreo,
                tipoTeclado = KeyboardType.Email,
                onPerderFoco = onCorreoPerderFoco
            )

            CampoContrasena(
                valor = contrasena,
                onValorCambia = onContrasenaCambia,
                etiqueta = stringResource(R.string.label_contrasena),
                visible = contrasenaVisible,
                onAlternarVisible = onAlternarVisible,
                error = errorContrasena
            )

            CampoContrasena(
                valor = confirmar,
                onValorCambia = onConfirmarCambia,
                etiqueta = stringResource(R.string.label_confirmar),
                visible = contrasenaVisible,
                onAlternarVisible = onAlternarVisible,
                error = errorConfirmar
            )

            NotaInfo(texto = stringResource(R.string.nota_rol_correo))

            BotonPrincipal(
                texto = stringResource(R.string.btn_crear_cuenta),
                onClick = onCrearCuenta,
                icono = R.drawable.ic_ingresar,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.pregunta_con_cuenta),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onIrALogin) {
                    Text(stringResource(R.string.accion_iniciar_sesion))
                }
            }

            Spacer(Modifier.size(Dimens.espacioMini))
            PieConfianza(texto = stringResource(R.string.pie_prototipo))
        }
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegistroScreenPreview() {
    PagoFlexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RegistroScreen(
                nombre = "Camila Rojas",
                rut = "15.834.207-3",
                correo = "crojas@correo.ejemplo",
                contrasena = "secreto",
                confirmar = "secreto",
                contrasenaVisible = false,
                onNombreCambia = {},
                onRutCambia = {},
                onCorreoCambia = {},
                onContrasenaCambia = {},
                onConfirmarCambia = {},
                onAlternarVisible = {},
                onCrearCuenta = {},
                onIrALogin = {}
            )
        }
    }
}
