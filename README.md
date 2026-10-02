# PagoFlex

Aplicación Android para organizar y pagar las cuentas del hogar (luz, agua, gas, internet y más) en un solo lugar. Muestra cuánto falta por pagar, avisa qué cuentas están por vencer y guarda el historial de pagos.

Proyecto desarrollado con **Kotlin** y **Jetpack Compose** para la asignatura de desarrollo de aplicaciones móviles.

## Estado del proyecto

En desarrollo (segunda evaluación).

- [x] Arquitectura MVVM por capas (`model`, `data`, `viewmodel`, `ui`, `utils`)
- [x] Sistema de diseño: colores de marca, tema claro y oscuro, medidas y estilos reutilizables
- [x] Componentes reutilizables: `BotonPrincipal`, `BotonSecundario`, `CampoTexto`, `CampoContrasena`
- [x] Lógica de negocio en los ViewModels (login, pagos, configuración)
- [x] Pruebas unitarias de la lógica
- [ ] Pantalla de Login
- [ ] Pantalla de Inicio
- [ ] Pantalla de Detalle de pago
- [ ] Pantalla de Historial
- [ ] Pantalla de Configuración
- [ ] Navegación entre pantallas

## Funcionalidades

| Pantalla | Qué permite |
|---|---|
| **Login** | Iniciar sesión con correo y contraseña, con validación y mensajes de error |
| **Inicio** | Ver el total pendiente y las cuentas por pagar, la que vence antes primero |
| **Detalle de pago** | Ver los datos de una cuenta y pagarla (queda registrada la fecha de pago) |
| **Historial** | Ver todos los pagos y filtrarlos por estado: pendiente, pagado o vencido |
| **Configuración** | Activar el modo oscuro, elegir con cuántos días de anticipación avisar (de 1 a 7) y cerrar sesión |

### Reglas de la aplicación

- Un pago puede estar `PENDIENTE`, `PAGADO` o `VENCIDO`.
- Un pago ya pagado no se puede pagar de nuevo.
- Los montos se muestran en pesos chilenos, por ejemplo `$25.990`.
- "Por vencer" son los pagos pendientes que vencen hoy o dentro de los próximos días de aviso configurados.
- El correo debe tener formato válido y la contraseña al menos 6 caracteres.

## Tecnologías

- Kotlin 2.2.10 y Jetpack Compose (BOM 2026.02.01) con Material 3
- ViewModel y estado con `mutableStateOf`
- Android Gradle Plugin 9.4.1
- minSdk 24 (Android 7.0), targetSdk 37

## Arquitectura

Patrón **MVVM**: las pantallas solo muestran el estado y avisan de las acciones del usuario; la lógica vive en los ViewModels y los datos en los repositorios.

```
app/src/main/java/com/example/pagoflex/
├── MainActivity.kt
├── model/          Pago, Usuario, EstadoPago, CategoriaPago
├── data/           PagoRepository, UsuarioRepository (datos de ejemplo en memoria)
├── viewmodel/      LoginViewModel, PagosViewModel, ConfiguracionViewModel
├── ui/
│   ├── screens/    Login, Inicio, DetallePago, Historial, Configuracion
│   ├── components/ BotonPrincipal, BotonSecundario, CampoTexto, CampoContrasena
│   ├── styles/     EstilosBoton, EstilosCampo, EstilosTarjeta
│   ├── theme/      Color, Dimens, Theme, Type
│   └── navigation/ Rutas
└── utils/          FormatoMoneda, FormatoFecha, Validaciones
```

## Cómo ejecutarla

1. Abre el proyecto con una versión reciente de **Android Studio** (con el SDK 37 instalado).
2. Espera a que termine la sincronización de Gradle.
3. Elige un emulador o un celular con Android 7.0 o superior y presiona **Run**.

Datos de prueba para iniciar sesión:

- Correo: `<completar>`
- Contraseña: `<completar>`

Los pagos son datos de ejemplo guardados en memoria: al cerrar la app vuelven a su estado inicial.

## Pruebas

Las pruebas unitarias cubren las fechas (incluido el cambio de horario), el pago de cuentas, el filtro, los totales y los límites de los ajustes.

```bash
./gradlew testDebugUnitTest
```

## Autor

<nombre y carrera>
