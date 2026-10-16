package com.example.pagoflex.ui.theme

import androidx.compose.ui.graphics.Color

// colores de marca (identidad oficial de PagoFlex: verde esmeralda + ambar)
val Primario = Color(0xFF0F9D7E)        // verde de la marca
val PrimarioOscuro = Color(0xFF085041)
val PrimarioClaro = Color(0xFF9FE1CB)
val Secundario = Color(0xFF097770)      // teal profundo de la marca
val Acento = Color(0xFFFAC775)          // el ambar de la moneda

// colores neutros (en verde-grisaceo, sin tinte morado)
val Blanco = Color(0xFFFFFFFF)
val FondoClaro = Color(0xFFF6FBF9)
val SuperficieClara = Color(0xFFFFFFFF)
val TextoOscuro = Color(0xFF121715)
val TextoClaro = Color(0xFFF6FBF9)
val FondoOscuro = Color(0xFF0D1211)
val SuperficieOscura = Color(0xFF16211D)
val GrisDeshabilitado = Color(0xFFB4BEB9)

// variantes de superficie, texto secundario y bordes. Se definen a proposito en
// verde-grisaceo para no caer en los grises con tinte morado por defecto de Material.
val SuperficieVarianteClara = Color(0xFFDCE6E1)
val TextoVarianteClaro = Color(0xFF3F4946)
val BordeClaro = Color(0xFF6E7B76)
val BordeVarianteClaro = Color(0xFFBEC9C3)
val SuperficieVarianteOscura = Color(0xFF3F4946)
val TextoVarianteOscuro = Color(0xFFBEC9C3)
val BordeOscuro = Color(0xFF889590)
val BordeVarianteOscuro = Color(0xFF3F4946)

// Familia de contenedores de superficie (la que usan las Card por defecto). Se
// definen en verde-gris para que ninguna Card caiga en el lavanda base de Material.
val SuperficieTenueClara = Color(0xFFD6E1DC)
val SuperficieBrillanteClara = Color(0xFFF6FBF9)
val ContenedorBajoMinimoClaro = Color(0xFFFFFFFF)
val ContenedorBajoClaro = Color(0xFFF0F6F3)
val ContenedorClaro = Color(0xFFEAF2EE)
val ContenedorAltoClaro = Color(0xFFE3ECE8)
val ContenedorAltoMaximoClaro = Color(0xFFDDE7E2)

val SuperficieTenueOscura = Color(0xFF0D1211)
val SuperficieBrillanteOscura = Color(0xFF333B38)
val ContenedorBajoMinimoOscuro = Color(0xFF080C0B)
val ContenedorBajoOscuro = Color(0xFF141F1B)
val ContenedorOscuro = Color(0xFF18231F)
val ContenedorAltoOscuro = Color(0xFF222E2A)
val ContenedorAltoMaximoOscuro = Color(0xFF2D3935)

// estados de un pago (EstadoPago)
val Exito = Color(0xFF2E9E5B)           // PAGADO
val ExitoFondo = Color(0xFFE3F5EA)
val Pendiente = Color(0xFF854F0B)       // PENDIENTE
val PendienteFondo = Color(0xFFFAEEDA)
val Vencido = Color(0xFFB3261E)         // VENCIDO
val VencidoFondo = Color(0xFFFCE8E6)
val VencidoTextoOscuro = Color(0xFFF2B8B5)  // rojo legible como texto sobre fondo oscuro
val VencidoSobreOscuro = Color(0xFF601410)
