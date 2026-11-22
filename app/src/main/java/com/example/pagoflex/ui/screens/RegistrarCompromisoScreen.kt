package com.example.pagoflex.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pagoflex.R
import com.example.pagoflex.model.Feriado
import com.example.pagoflex.ui.components.BotonPrincipal
import com.example.pagoflex.ui.components.BotonSecundario
import com.example.pagoflex.ui.components.CampoTexto
import com.example.pagoflex.ui.components.NotaInfo
import com.example.pagoflex.ui.styles.EstilosCampo
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.utils.Validaciones
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarCompromisoScreen(
    alVolver: () -> Unit,
    onRegistrar: (deudor: String, rut: String, concepto: String, totalCuotas: Int, cuotaInicial: Int, montoCuota: Int, fecha: String) -> Unit,
    errorServidor: String? = null,
    nombreDeudor: String? = null,
    onRutCambia: (String) -> Unit = {},
    feriados: List<Feriado> = emptyList(),
    errorFeriados: String? = null,
    onAnioDeLaFecha: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var deudor by rememberSaveable { mutableStateOf("") }
    var rut by rememberSaveable { mutableStateOf("") }
    var concepto by rememberSaveable { mutableStateOf("") }
    var totalCuotas by rememberSaveable { mutableStateOf("") }
    var cuotaInicial by rememberSaveable { mutableStateOf("1") }
    var monto by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var intentoEnvio by remember { mutableStateOf(false) }

    var registrando by remember { mutableStateOf(false) }

    var mostrarCalendario by remember { mutableStateOf(false) }

    fun formularioValido(): Boolean {
        val fechaIngresada = fecha.replace("/", "-")
        return deudor.isNotBlank() &&
            Validaciones.nombreSoloLetras(deudor) &&
            Validaciones.rutValido(rut) &&
            concepto.isNotBlank() &&
            Validaciones.cantidadCuotasValida(totalCuotas) &&
            Validaciones.cuotaInicialValida(cuotaInicial, totalCuotas) &&
            Validaciones.montoValido(monto) &&
            Validaciones.fechaValida(fechaIngresada) &&
            Validaciones.fechaNoAnteriorAHoy(fechaIngresada)
    }

    LaunchedEffect(errorServidor) {
        if (errorServidor != null) registrando = false
    }

    LaunchedEffect(nombreDeudor) {
        if (nombreDeudor != null) deudor = nombreDeudor
    }

    LaunchedEffect(registrando) {
        if (registrando) {
            delay(1000)
            if (!formularioValido()) {
                registrando = false
                return@LaunchedEffect
            }
            onRegistrar(
                deudor,
                rut,
                concepto,
                totalCuotas.trim().toInt(),
                cuotaInicial.trim().toInt(),
                monto.trim().toInt(),
                fecha.replace("/", "-")
            )
        }
    }

    val errorDeudor = when {
        !intentoEnvio -> null
        deudor.isBlank() -> "Ingresa el nombre del deudor"
        !Validaciones.nombreSoloLetras(deudor) -> "El nombre no puede llevar números"
        else -> null
    }
    val errorRut = when {
        !intentoEnvio -> null
        rut.isBlank() -> "Ingresa el RUT del deudor"
        !Validaciones.rutValido(rut) -> "RUT inválido"
        else -> null
    }
    val errorConcepto = if (intentoEnvio && concepto.isBlank()) "Ingresa el concepto" else null
    val errorTotalCuotas = if (intentoEnvio && !Validaciones.cantidadCuotasValida(totalCuotas))
        "Ingresa una cantidad entre 1 y ${Validaciones.MAXIMO_CUOTAS}" else null
    val errorCuotaInicial = if (intentoEnvio && !Validaciones.cuotaInicialValida(cuotaInicial, totalCuotas))
        "Debe estar entre 1 y la cantidad de cuotas" else null
    val errorMonto = if (intentoEnvio && !Validaciones.montoValido(monto)) "Monto inválido (entero positivo)" else null

    val fechaConGuiones = fecha.replace("/", "-")
    val errorFecha = when {
        !intentoEnvio -> null
        !Validaciones.fechaValida(fechaConGuiones) -> "Ingresa una fecha válida (DD/MM/AAAA)"
        !Validaciones.fechaNoAnteriorAHoy(fechaConGuiones) -> "La fecha no puede ser anterior a hoy"
        else -> null
    }

    val fechaCompleta = Validaciones.fechaValida(fechaConGuiones)
    val feriadoElegido = if (fechaCompleta) Validaciones.feriadoEn(fechaConGuiones, feriados) else null
    val proximoFeriado = Validaciones.proximoFeriado(feriados)

    LaunchedEffect(fechaConGuiones) {
        if (fechaCompleta) onAnioDeLaFecha(fechaConGuiones.takeLast(4).toInt())
    }

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
                .consumeWindowInsets(padding)
                .imePadding()
                .padding(Dimens.espacioPantalla)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioChico)
        ) {
            CampoTexto(
                valor = rut,
                onValorCambia = {
                    rut = it
                    onRutCambia(it)
                },
                etiqueta = "RUT del deudor",
                icono = R.drawable.ic_otro,
                error = errorRut
            )
            CampoTexto(
                valor = deudor,
                onValorCambia = { deudor = it },
                etiqueta = "Nombre del deudor",
                icono = R.drawable.ic_perfil,
                error = errorDeudor,
                ayuda = if (nombreDeudor != null) "Nombre de la cuenta PagoFlex con este RUT" else null,
                soloLectura = nombreDeudor != null
            )
            CampoTexto(
                valor = concepto,
                onValorCambia = { concepto = it },
                etiqueta = "Concepto",
                icono = R.drawable.ic_otro,
                error = errorConcepto
            )
            CampoTexto(
                valor = totalCuotas,
                onValorCambia = { totalCuotas = it },
                etiqueta = "Cantidad de cuotas",
                icono = R.drawable.ic_otro,
                error = errorTotalCuotas,
                tipoTeclado = KeyboardType.Number
            )
            CampoTexto(
                valor = cuotaInicial,
                onValorCambia = { cuotaInicial = it },
                etiqueta = "Cuota inicial",
                icono = R.drawable.ic_otro,
                error = errorCuotaInicial,
                tipoTeclado = KeyboardType.Number
            )
            CampoTexto(
                valor = monto,
                onValorCambia = { monto = it },
                etiqueta = "Monto de cada cuota (pesos)",
                icono = R.drawable.ic_pagar,
                error = errorMonto,
                tipoTeclado = KeyboardType.Number
            )

            OutlinedTextField(
                value = TextFieldValue(fecha, selection = TextRange(fecha.length)),
                onValueChange = { fecha = formatearFechaEntrada(it.text) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Vencimiento de la cuota inicial") },
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

            when {
                feriadoElegido != null -> NotaInfo(
                    texto = "El ${feriadoElegido.fecha} es feriado: ${feriadoElegido.nombre}. Revisa si la cuota debe vencer ese día."
                )
                proximoFeriado != null -> NotaInfo(
                    texto = "Próximo feriado: ${proximoFeriado.fecha}, ${proximoFeriado.nombre}",
                    icono = R.drawable.ic_dias_aviso
                )
                errorFeriados != null -> Text(
                    text = errorFeriados,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (errorServidor != null) {
                Text(
                    text = errorServidor,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            BotonPrincipal(
                texto = if (registrando) "Registrando…" else "Registrar",
                onClick = {
                    intentoEnvio = true
                    if (formularioValido()) registrando = true
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

        if (mostrarCalendario) {
            val estadoCalendario = remember {
                DatePickerState(
                    locale = Locale.forLanguageTag("es-CL"),
                    selectableDates = object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                            val formato = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                            formato.timeZone = TimeZone.getTimeZone("UTC")
                            return Validaciones.fechaNoAnteriorAHoy(formato.format(Date(utcTimeMillis)))
                        }
                    }
                )
            }
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
                DatePicker(
                    state = estadoCalendario,
                    title = {
                        Text(
                            text = "Elige la fecha de vencimiento",
                            modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)
                        )
                    },
                    headline = {
                        val elegida = estadoCalendario.selectedDateMillis?.let { millis ->
                            val formato = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                            formato.timeZone = TimeZone.getTimeZone("UTC")
                            formato.format(Date(millis))
                        }
                        Text(
                            text = elegida ?: "Sin fecha elegida",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp)
                        )
                    }
                )
            }
        }
    }
}

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
        RegistrarCompromisoScreen(alVolver = {}, onRegistrar = { _, _, _, _, _, _, _ -> })
    }
}
