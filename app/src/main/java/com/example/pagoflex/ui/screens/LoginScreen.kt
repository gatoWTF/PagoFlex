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
import com.example.pagoflex.ui.components.PieConfianza
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme

/**
 * Pantalla de inicio de sesion (RF-01). Es "sin estado": recibe los valores y
 * avisa de las acciones; la logica vive en el ViewModel (se conecta en la
 * siguiente etapa). Al ingresar, el rol se deduce del correo.
 */
@Composable
fun LoginScreen(
    correo: String,
    contrasena: String,
    contrasenaVisible: Boolean,
    onCorreoCambia: (String) -> Unit,
    onContrasenaCambia: (String) -> Unit,
    onAlternarVisible: () -> Unit,
    onIngresar: () -> Unit,
    onIrARegistro: () -> Unit,
    modifier: Modifier = Modifier,
    errorCorreo: String? = null,
    errorContrasena: String? = null,
    errorGeneral: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        EncabezadoMarca(
            titulo = stringResource(R.string.app_name),
            subtitulo = stringResource(R.string.marca_subtitulo_login)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.espacioPantalla),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)
        ) {
            Text(
                text = stringResource(R.string.titulo_login),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            CampoTexto(
                valor = correo,
                onValorCambia = onCorreoCambia,
                etiqueta = stringResource(R.string.label_correo),
                icono = R.drawable.ic_correo,
                error = errorCorreo,
                tipoTeclado = KeyboardType.Email
            )

            CampoContrasena(
                valor = contrasena,
                onValorCambia = onContrasenaCambia,
                etiqueta = stringResource(R.string.label_contrasena),
                visible = contrasenaVisible,
                onAlternarVisible = onAlternarVisible,
                error = errorContrasena
            )

            if (errorGeneral != null) {
                Text(
                    text = errorGeneral,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            BotonPrincipal(
                texto = stringResource(R.string.btn_ingresar),
                onClick = onIngresar,
                icono = R.drawable.ic_ingresar,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.pregunta_sin_cuenta),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onIrARegistro) {
                    Text(stringResource(R.string.accion_crear_cuenta))
                }
            }

            Spacer(Modifier.size(Dimens.espacioChico))
            PieConfianza(texto = stringResource(R.string.pie_confianza))
        }
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenPreview() {
    PagoFlexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginScreen(
                correo = "crojas@correo.ejemplo",
                contrasena = "secreto",
                contrasenaVisible = false,
                onCorreoCambia = {},
                onContrasenaCambia = {},
                onAlternarVisible = {},
                onIngresar = {},
                onIrARegistro = {}
            )
        }
    }
}
