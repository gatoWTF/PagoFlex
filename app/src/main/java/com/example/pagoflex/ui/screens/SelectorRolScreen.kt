package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagoflex.R
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme

// Selector de rol: reemplaza el login real con una sesion simulada (RF-01, FA-01).
// Por ahora cada rol entra con un usuario fijo de la semilla, para poder probar.
// Mas adelante el rol se declara segun el cargo de quien inicia sesion, sin dejar
// elegir la cuenta de otra persona.
@Composable
fun SelectorRolScreen(
    alElegirRol: (RolUsuario) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.espacioPantalla),
        verticalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)
    ) {
        Spacer(Modifier.size(Dimens.espacioGrande))
        Text(
            text = "PagoFlex",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Elige tu perfil para esta sesión",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.size(Dimens.espacioChico))

        PerfilesDemo.todos.forEach { perfil ->
            TarjetaRol(perfil = perfil, onClick = { alElegirRol(perfil.rol) })
        }

        Spacer(Modifier.weight(1f))
        Text(
            text = "Sesión simulada con fines de demostración. No requiere contraseña.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Tarjeta seleccionable de un rol: icono, nombre del rol y usuario fijo de demo.
@Composable
private fun TarjetaRol(perfil: PerfilDemo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.radioTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevacionTarjeta)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.espacioMedio),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(perfil.icono),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(Dimens.tamanoIcono)
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = perfil.rol.etiqueta,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = perfil.usuarioFijo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_ingresar),
                contentDescription = "Entrar como " + perfil.rol.etiqueta,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.tamanoIcono)
            )
        }
    }
}

// Usuario fijo de demo por cada rol (tomado del Anexo 9). Solo para pruebas.
private data class PerfilDemo(
    val rol: RolUsuario,
    val usuarioFijo: String,
    @DrawableRes val icono: Int
)

private object PerfilesDemo {
    val todos = listOf(
        PerfilDemo(RolUsuario.USUARIO_FINAL, "Camila Rojas", R.drawable.ic_perfil),
        PerfilDemo(RolUsuario.AGENTE, "Andrés Lagos · Soporte", R.drawable.ic_avisos),
        PerfilDemo(RolUsuario.EJECUTIVO, "Claudia Henríquez · Crédito Andino", R.drawable.ic_otro)
    )
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SelectorRolScreenPreview() {
    PagoFlexTheme {
        SelectorRolScreen(alElegirRol = {})
    }
}
