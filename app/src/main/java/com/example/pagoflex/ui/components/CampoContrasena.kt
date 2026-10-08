package com.example.pagoflex.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.ui.styles.EstilosCampo
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme

/**
 * Campo de contrasena con puntos y boton "ojo".
 * visualTransformation cambia COMO se muestra el texto, no lo que vale:
 * PasswordVisualTransformation() muestra puntos y VisualTransformation.None
 * muestra el texto. Si la contrasena se ve o no es ESTADO: lo decide el ViewModel.
 */
@Composable
fun CampoContrasena(
    valor: String,
    onValorCambia: (String) -> Unit,
    etiqueta: String,
    visible: Boolean,
    onAlternarVisible: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorCambia,
        modifier = modifier.fillMaxWidth(),
        label = { Text(etiqueta) },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_candado),
                contentDescription = null,
                modifier = Modifier.size(Dimens.tamanoIcono)
            )
        },
        trailingIcon = {                                     // icono a la DERECHA, presionable
            IconButton(onClick = onAlternarVisible) {
                Icon(
                    painter = painterResource(
                        if (visible) R.drawable.ic_ojo_tachado else R.drawable.ic_ojo
                    ),
                    contentDescription = stringResource(
                        if (visible) R.string.cd_ocultar_contrasena else R.string.cd_mostrar_contrasena
                    ),
                    modifier = Modifier.size(Dimens.tamanoIcono)
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        isError = error != null,
        supportingText = { if (error != null) Text(error) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done                       // boton "Listo" en el teclado
        ),
        shape = EstilosCampo.forma,
        colors = EstilosCampo.colores()
    )
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CampoContrasenaPreview() {
    PagoFlexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CampoContrasena(
                valor = "secreto",
                onValorCambia = {},
                etiqueta = "Contraseña",
                visible = false,
                onAlternarVisible = {},
                modifier = Modifier.padding(Dimens.espacioMedio)
            )
        }
    }
}
