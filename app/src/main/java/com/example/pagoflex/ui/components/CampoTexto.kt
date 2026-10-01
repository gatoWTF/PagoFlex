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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.ui.styles.EstilosCampo
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme

/**
 * Campo de texto reutilizable (el <input> de HTML).
 * En Compose un campo NO guarda su propio texto: "valor" viene del estado del
 * ViewModel y "onValorCambia" avisa cada vez que el usuario escribe. Si no se
 * actualiza el estado ahí, el campo no cambia ("estado elevado").
 * @param error       mensaje bajo el campo; null = sin error.
 * @param tipoTeclado Text, Email, Number, Phone... (cambia el teclado del celular).
 */
@Composable
fun CampoTexto(
    valor: String,
    onValorCambia: (String) -> Unit,
    etiqueta: String,
    @DrawableRes icono: Int,
    modifier: Modifier = Modifier,
    error: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorCambia,
        modifier = modifier.fillMaxWidth(),
        label = { Text(etiqueta) },                          // texto flotante (como <label>)
        leadingIcon = {                                      // ícono a la izquierda
            Icon(
                painter = painterResource(id = icono),
                contentDescription = null,
                modifier = Modifier.size(Dimens.tamanoIcono)
            )
        },
        isError = error != null,                             // borde rojo si hay error
        supportingText = { if (error != null) Text(error) }, // texto de ayuda bajo el campo
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado,
            imeAction = ImeAction.Next                       // botón "Siguiente" en el teclado
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
