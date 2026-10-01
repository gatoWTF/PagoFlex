package com.example.pagoflex.ui.styles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.theme.Blanco
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.GrisDeshabilitado
import com.example.pagoflex.ui.theme.Primario

object EstilosBoton {
    val forma = RoundedCornerShape(Dimens.radioBoton)                    // border-radius
    val relleno = PaddingValues(horizontal = 20.dp, vertical = 12.dp)    // padding

    // botones principales: relleno morado, texto blanco
    @Composable
    fun coloresPrincipal(): ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Primario,                                       // background-color
        contentColor = Blanco,                                           // color
        disabledContainerColor = GrisDeshabilitado,
        disabledContentColor = Blanco
    )

    // botones secundarios: transparentes, texto del color primario del tema
    @Composable
    fun coloresSecundario(): ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.primary
    )

    // borde de los botones secundarios
    @Composable
    fun bordeSecundario(): BorderStroke = BorderStroke(
        Dimens.bordeBoton, MaterialTheme.colorScheme.primary
    )

    // sombra del botón principal (se hunde al presionar)
    @Composable
    fun elevacion(): ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = Dimens.elevacionBoton,
        pressedElevation = 1.dp
    )
}

// funciona como una clase CSS: se aplica con Modifier.estiloAltoBoton()
fun Modifier.estiloAltoBoton(): Modifier = this.height(Dimens.alturaBoton)
