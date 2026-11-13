package com.example.pagoflex.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.pagoflex.R
import com.example.pagoflex.ui.components.BotonPrincipal
import com.example.pagoflex.ui.components.CampoTexto
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.viewmodel.EditarPerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(
    viewModel: EditarPerfilViewModel,
    idUsuario: Long?,
    rut: String,
    alVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar mis datos") },
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
            CampoTexto(
                valor = viewModel.nombre,
                onValorCambia = viewModel::cambiarNombre,
                etiqueta = "Nombre",
                icono = R.drawable.ic_perfil,
                error = viewModel.errorNombre
            )
            CampoTexto(
                valor = viewModel.correo,
                onValorCambia = viewModel::cambiarCorreo,
                etiqueta = "Correo electrónico",
                icono = R.drawable.ic_correo,
                error = viewModel.errorCorreo,
                tipoTeclado = KeyboardType.Email
            )
            Text(
                text = "RUT: $rut",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "El RUT no se puede cambiar porque tus compromisos están asociados a él.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            val error = viewModel.errorGeneral
            if (error != null) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }
            BotonPrincipal(
                texto = "Guardar cambios",
                onClick = { viewModel.guardar(idUsuario, rut) },
                cargando = viewModel.cargando,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}