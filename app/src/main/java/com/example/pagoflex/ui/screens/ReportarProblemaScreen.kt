package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.EstadoCompromiso
import com.example.pagoflex.ui.components.BotonPrincipal
import com.example.pagoflex.ui.components.BotonSecundario
import com.example.pagoflex.ui.styles.EstilosCampo
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme

// Motivos posibles de un reporte de problema (Anexo 7).
private val MOTIVOS = listOf(
    "No reconozco este cobro",
    "El monto no corresponde",
    "Ya pagué por otro medio",
    "Otro"
)

private const val LARGO_MINIMO_DESCRIPCION = 10

// Reportar un problema sobre un compromiso (RF-09). Formulario con validaciones:
// hay que elegir un motivo y escribir una descripcion suficiente.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportarProblemaScreen(
    compromiso: Compromiso?,
    alVolver: () -> Unit,
    alEnviar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var motivo by rememberSaveable { mutableStateOf<String?>(null) }
    var descripcion by rememberSaveable { mutableStateOf("") }
    // Las validaciones se muestran recien al intentar enviar, para no molestar antes.
    var intentoEnvio by remember { mutableStateOf(false) }

    val errorMotivo = if (intentoEnvio && motivo == null) "Selecciona un motivo" else null
    val errorDescripcion = when {
        !intentoEnvio -> null
        descripcion.isBlank() -> "Describe el problema"
        descripcion.trim().length < LARGO_MINIMO_DESCRIPCION ->
            "Cuéntanos un poco más (mínimo $LARGO_MINIMO_DESCRIPCION caracteres)"
        else -> null
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Reportar un problema") },
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
        if (compromiso == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontro el compromiso.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.espacioPantalla)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioChico)
        ) {
            Text(
                text = "Compromiso: " + compromiso.concepto,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "¿Cuál es el problema?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = Dimens.espacioChico)
            )

            MOTIVOS.forEach { opcion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = motivo == opcion,
                            onClick = { motivo = opcion }
                        )
                        .padding(vertical = Dimens.espacioMini),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = motivo == opcion,
                        onClick = { motivo = opcion }
                    )
                    Text(
                        text = opcion,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            if (errorMotivo != null) {
                Text(
                    text = errorMotivo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.espacioChico),
                label = { Text("Describe el problema") },
                minLines = 3,
                isError = errorDescripcion != null,
                supportingText = { if (errorDescripcion != null) Text(errorDescripcion) },
                shape = EstilosCampo.forma,
                colors = EstilosCampo.colores()
            )

            BotonPrincipal(
                texto = "Enviar reporte",
                onClick = {
                    intentoEnvio = true
                    val valido = motivo != null &&
                        descripcion.trim().length >= LARGO_MINIMO_DESCRIPCION
                    if (valido) alEnviar()
                },
                icono = R.drawable.ic_avisos,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.espacioChico)
            )
            BotonSecundario(
                texto = "Cancelar",
                onClick = alVolver,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Al enviar, el compromiso queda En revisión mientras un agente lo revisa.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ReportarProblemaScreenPreview() {
    PagoFlexTheme {
        ReportarProblemaScreen(
            compromiso = Compromiso(
                folio = "CP-2026-0102",
                empresa = "Club Los Halcones",
                rubro = "Club deportivo",
                concepto = "Cuota social octubre",
                cuota = "10/12",
                monto = 15000,
                fechaVencimiento = "05-10-2026",
                estado = EstadoCompromiso.VENCIDO,
                recargo = 2000
            ),
            alVolver = {},
            alEnviar = {}
        )
    }
}
