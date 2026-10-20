package com.example.pagoflex.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.ui.styles.EstilosCampo
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme

@Composable
fun CampoTexto(
    valor: String,
    onValorCambia: (String) -> Unit,
    etiqueta: String,
    @DrawableRes icono: Int,
    modifier: Modifier = Modifier,
    error: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    onPerderFoco: (() -> Unit)? = null
) {

    var tuvoFoco by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = valor,
        onValueChange = onValorCambia,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { estado ->
                if (estado.isFocused) {
                    tuvoFoco = true
                } else if (tuvoFoco) {
                    tuvoFoco = false
                    onPerderFoco?.invoke()
                }
            },
        label = { Text(etiqueta) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = icono),
                contentDescription = null,
                modifier = Modifier.size(Dimens.tamanoIcono)
            )
        },
        isError = error != null,
        supportingText = { if (error != null) Text(error) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado,
            imeAction = ImeAction.Next
        ),
        shape = EstilosCampo.forma,
        colors = EstilosCampo.colores()
    )
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CampoTextoPreview() {
    PagoFlexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CampoTexto(
                valor = "ana@",
                onValorCambia = {},
                etiqueta = "Correo electrónico",
                icono = R.drawable.ic_correo,
                error = "Ingresa un correo válido",
                tipoTeclado = KeyboardType.Email,
                modifier = Modifier.padding(Dimens.espacioMedio)
            )
        }
    }
}
