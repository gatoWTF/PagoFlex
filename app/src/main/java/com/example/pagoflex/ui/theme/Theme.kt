package com.example.pagoflex.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimarioClaro,
    onPrimary = PrimarioOscuro,
    primaryContainer = PrimarioOscuro,
    onPrimaryContainer = PrimarioClaro,
    secondary = Secundario,
    onSecondary = Blanco,
    tertiary = Acento,
    onTertiary = TextoOscuro,
    background = FondoOscuro,
    onBackground = TextoClaro,
    surface = SuperficieOscura,
    onSurface = TextoClaro,
    // Variantes y bordes en verde-grisaceo (evitan los morados por defecto de Material).
    surfaceVariant = SuperficieVarianteOscura,
    onSurfaceVariant = TextoVarianteOscuro,
    outline = BordeOscuro,
    outlineVariant = BordeVarianteOscuro,
    inverseSurface = ExitoFondo,
    inverseOnSurface = SuperficieOscura,
    // Contenedores de estado: misma paleta que las pildoras de pago, igual en ambos temas.
    error = VencidoTextoOscuro,
    onError = VencidoSobreOscuro,
    errorContainer = VencidoFondo,
    onErrorContainer = Vencido,
    secondaryContainer = ExitoFondo,
    onSecondaryContainer = Exito,
    tertiaryContainer = PendienteFondo,
    onTertiaryContainer = Pendiente,
    // Contenedores de superficie (default de las Card) en verde-gris.
    surfaceDim = SuperficieTenueOscura,
    surfaceBright = SuperficieBrillanteOscura,
    surfaceContainerLowest = ContenedorBajoMinimoOscuro,
    surfaceContainerLow = ContenedorBajoOscuro,
    surfaceContainer = ContenedorOscuro,
    surfaceContainerHigh = ContenedorAltoOscuro,
    surfaceContainerHighest = ContenedorAltoMaximoOscuro
)

private val LightColorScheme = lightColorScheme(
    primary = Primario,
    onPrimary = Blanco,
    primaryContainer = PrimarioClaro,
    onPrimaryContainer = PrimarioOscuro,
    secondary = Secundario,
    onSecondary = Blanco,
    tertiary = Acento,
    onTertiary = TextoOscuro,
    background = FondoClaro,
    onBackground = TextoOscuro,
    surface = SuperficieClara,
    onSurface = TextoOscuro,
    // Variantes y bordes en verde-grisaceo (evitan los morados por defecto de Material).
    surfaceVariant = SuperficieVarianteClara,
    onSurfaceVariant = TextoVarianteClaro,
    outline = BordeClaro,
    outlineVariant = BordeVarianteClaro,
    inverseSurface = SuperficieOscura,
    inverseOnSurface = TextoClaro,
    // Contenedores de estado: misma paleta que las pildoras de pago, igual en ambos temas.
    error = Vencido,
    onError = Blanco,
    errorContainer = VencidoFondo,
    onErrorContainer = Vencido,
    secondaryContainer = ExitoFondo,
    onSecondaryContainer = Exito,
    tertiaryContainer = PendienteFondo,
    onTertiaryContainer = Pendiente,
    // Contenedores de superficie (default de las Card) en verde-gris.
    surfaceDim = SuperficieTenueClara,
    surfaceBright = SuperficieBrillanteClara,
    surfaceContainerLowest = ContenedorBajoMinimoClaro,
    surfaceContainerLow = ContenedorBajoClaro,
    surfaceContainer = ContenedorClaro,
    surfaceContainerHigh = ContenedorAltoClaro,
    surfaceContainerHighest = ContenedorAltoMaximoClaro
)

@Composable
fun PagoFlexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Con true, Android 12+ ignora los colores de marca y usa los del fondo de pantalla
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}