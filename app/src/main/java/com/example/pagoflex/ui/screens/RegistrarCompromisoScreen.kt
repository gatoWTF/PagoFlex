package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.ui.components.BotonPrincipal
import com.example.pagoflex.ui.components.BotonSecundario
import com.example.pagoflex.ui.components.CampoTexto
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.utils.Validaciones
import kotlinx.coroutines.delay

// Formulario para registrar un compromiso (RF-17). Valida nombre, RUT (modulo 11),
// concepto, monto entero en pesos y fecha DD-MM-AAAA (RN-17, RN-18).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarCompromisoScreen(
    alVolver: () -> Unit,
    onRegistrar: (deudor: String, rut: String, concepto: String, monto: Int, fecha: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var deudor by rememberSaveable { mutableStateOf("") }
    var rut by rememberSaveable { mutableStateOf("") }
    var concepto by rememberSaveable { mutableStateOf("") }
    var monto by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var intentoEnvio by remember { mutableStateOf(false) }
    // Animacion 8: tras validar, el boton muestra un spinner antes de registrar.
    var registrando by remember { mutableStateOf(false) }

    LaunchedEffect(registrando) {
        if (registrando) {
            delay(1000)
            onRegistrar(deudor, rut, concepto, monto.trim().toInt(), fecha)
        }
    }

    val errorDeudor = if (intentoEnvio && deudor.isBlank()) "Ingresa el nombre del deudor" else null
    val errorRut = if (intentoEnvio && !Validaciones.rutValido(rut)) "RUT inválido" else null
    val errorConcepto = if (intentoEnvio && concepto.isBlank()) "Ingresa el concepto" else null
    val errorMonto = if (intentoEnvio && !Validaciones.montoValido(monto)) "Monto inválido (entero positivo)" else null
    val errorFecha = if (intentoEnvio && !Validaciones.fechaValida(fecha)) "Fecha inválida (DD-MM-AAAA)" else null

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Registrar compromiso") },
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
                .padding(Dimens.espacioPantalla)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioChico)
        ) {
            CampoTexto(
                valor = deudor,
                onValorCambia = { deudor = it },
                etiqueta = "Nombre del deudor",
                icono = R.drawable.ic_perfil,
                error = errorDeudor
            )
            CampoTexto(
                valor = rut,
                onValorCambia = { rut = it },
                etiqueta = "RUT del deudor",
                icono = R.drawable.ic_otro,
                error = errorRut
            )
            CampoTexto(
                valor = concepto,
                onValorCambia = { concepto = it },
                etiqueta = "Concepto",
                icono = R.drawable.ic_otro,
                error = errorConcepto
            )
            CampoTexto(
                valor = monto,
                onValorCambia = { monto = it },
                etiqueta = "Monto (pesos)",
                icono = R.drawable.ic_pagar,
                error = errorMonto,
                tipoTeclado = KeyboardType.Number
            )
            CampoTexto(
                valor = fecha,
                onValorCambia = { fecha = it },
                etiqueta = "Vencimiento (DD-MM-AAAA)",
                icono = R.drawable.ic_dias_aviso,
                error = errorFecha
            )

            BotonPrincipal(
                texto = if (registrando) "Registrando…" else "Registrar",
                onClick = {
                    intentoEnvio = true
                    val valido = deudor.isNotBlank() &&
                        Validaciones.rutValido(rut) &&
                        concepto.isNotBlank() &&
                        Validaciones.montoValido(monto) &&
                        Validaciones.fechaValida(fecha)
                    if (valido) registrando = true
                },
                cargando = registrando,
                icono = R.drawable.ic_ingresar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.espacioChico)
            )
            BotonSecundario(
                texto = "Cancelar",
                onClick = alVolver,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegistrarCompromisoScreenPreview() {
    PagoFlexTheme {
        RegistrarCompromisoScreen(alVolver = {}, onRegistrar = { _, _, _, _, _ -> })
    }
}
