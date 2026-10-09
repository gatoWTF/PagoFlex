package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import com.example.pagoflex.ui.styles.EstilosCampo
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.utils.Validaciones
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
    // Selector de fecha: evita escribir y errores de formato.
    var mostrarCalendario by remember { mutableStateOf(false) }

    LaunchedEffect(registrando) {
        if (registrando) {
            delay(1000)
            onRegistrar(deudor, rut, concepto, monto.trim().toInt(), fecha.replace("/", "-"))
        }
    }

    val errorDeudor = if (intentoEnvio && deudor.isBlank()) "Ingresa el nombre del deudor" else null
    val errorRut = if (intentoEnvio && !Validaciones.rutValido(rut)) "RUT inválido" else null
    val errorConcepto = if (intentoEnvio && concepto.isBlank()) "Ingresa el concepto" else null
    val errorMonto = if (intentoEnvio && !Validaciones.montoValido(monto)) "Monto inválido (entero positivo)" else null
    // La fecha se muestra con "/", pero se valida y guarda con "-" como el resto de la app.
    val fechaConGuiones = fecha.replace("/", "-")
    val errorFecha = if (intentoEnvio && !Validaciones.fechaValida(fechaConGuiones)) "Ingresa una fecha válida (DD/MM/AAAA)" else null

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
            // Fecha: se escribe con los numeros y los "/" se ponen solos (mascara),
            // o se elige tocando el icono de calendario.
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = formatearFechaEntrada(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Vencimiento (DD/MM/AAAA)") },
                placeholder = { Text("DD/MM/AAAA") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_dias_aviso),
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.tamanoIcono)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { mostrarCalendario = true }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_dias_aviso),
                            contentDescription = "Elegir en el calendario",
                            modifier = Modifier.size(Dimens.tamanoIcono)
                        )
                    }
                },
                isError = errorFecha != null,
                supportingText = { if (errorFecha != null) Text(errorFecha) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = EstilosCampo.forma,
                colors = EstilosCampo.colores()
            )

            BotonPrincipal(
                texto = if (registrando) "Registrando…" else "Registrar",
                onClick = {
                    intentoEnvio = true
                    val valido = deudor.isNotBlank() &&
                        Validaciones.rutValido(rut) &&
                        concepto.isNotBlank() &&
                        Validaciones.montoValido(monto) &&
                        Validaciones.fechaValida(fechaConGuiones)
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

        // Calendario para elegir la fecha; al aceptar, la deja en formato DD-MM-AAAA.
        if (mostrarCalendario) {
            val estadoCalendario = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { mostrarCalendario = false },
                confirmButton = {
                    TextButton(onClick = {
                        estadoCalendario.selectedDateMillis?.let { millis ->
                            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                            formato.timeZone = TimeZone.getTimeZone("UTC")
                            fecha = formato.format(Date(millis))
                        }
                        mostrarCalendario = false
                    }) { Text("Aceptar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
                }
            ) {
                DatePicker(state = estadoCalendario)
            }
        }
    }
}

// Toma lo que escribe la persona, deja solo digitos (max 8) y pone los "/" solos:
// "25102026" -> "25/10/2026". Asi no hay que escribir los separadores.
private fun formatearFechaEntrada(entrada: String): String {
    val digitos = entrada.filter { it.isDigit() }.take(8)
    return buildString {
        for (i in digitos.indices) {
            append(digitos[i])
            if ((i == 1 || i == 3) && i != digitos.lastIndex) append('/')
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
