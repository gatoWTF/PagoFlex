# PagoFlex

## 1. Nombre del proyecto

**PagoFlex**: app móvil de una fintech de recaudación y cobranza. Las personas ven y pagan sus compromisos de pago (créditos en cuotas, arriendos, mensualidades) con las empresas clientes de PagoFlex.

App en Kotlin con Jetpack Compose (MVVM), conectada por Retrofit a microservicios Spring Boot: [gatoWTF/Pagoflex-Microservicios](https://github.com/gatoWTF/Pagoflex-Microservicios).

## 2. Integrantes

- Gabriel Antonio Molina Apablaza

## 3. Funcionalidades

**Usuario final**
- Inicio de sesión contra el servidor y registro de cuenta nueva.
- Situación del mes: total por pagar, próximo vencimiento y cuotas vencidas.
- Detalle de cada cuota, pago con comprobante y generación de la cuota siguiente.
- Historial de pagos e indicador de cumplimiento.
- Reportar un problema sobre un compromiso.
- Perfil: foto con la cámara o la galería, editar nombre y correo, datos de contacto, cambiar contraseña y eliminar la cuenta.
- Avisos de vencimiento con notificaciones.
- Tema claro u oscuro.

**Ejecutivo de empresa**
- Cobranza de su empresa con filtros.
- Registrar créditos en cuotas, con aviso de feriados en la fecha de vencimiento.
- Anular compromisos con motivo.

**Agente PagoFlex**
- Bandeja de reportes: tomar y resolver; al resolver, la cuota vuelve a Pendiente o Vencido.

**Cuentas de ejemplo**: `crojas@correo.ejemplo` (usuario final), `chenriquez@creditoandino.ejemplo` (ejecutivo) y `alagos@pagoflex.ejemplo` (agente).

## 4. Endpoints utilizados

**API externa** (Retrofit propio, `RetrofitFeriados`)

| Método | URL | Uso en la app |
|---|---|---|
| GET | `https://api.boostr.cl/holidays/{año}.json` | Feriados de Chile en el formulario del ejecutivo |

**Microservicios** (todos a través del API Gateway, `https://0k707gs4-8080.brs.devtunnels.ms`)

| Método | Ruta | Uso en la app |
|---|---|---|
| POST | `/api/usuarios/login` | Iniciar sesión |
| POST | `/api/usuarios` | Crear cuenta |
| GET | `/api/usuarios/{id}` | Datos de la cuenta |
| GET | `/api/usuarios/titular?rut=` | Nombre del deudor al registrar un crédito |
| PUT | `/api/usuarios/{id}` | Editar nombre y correo |
| PUT | `/api/usuarios/{id}/contacto` | Guardar datos de contacto |
| PUT | `/api/usuarios/{id}/contrasena` | Cambiar contraseña |
| DELETE | `/api/usuarios/{id}` | Eliminar la cuenta |
| GET | `/api/compromisos?rut=` | Compromisos del usuario |
| GET | `/api/compromisos?empresa=` | Cobranza de la empresa del ejecutivo |
| GET | `/api/compromisos/{folio}` | Detalle de un compromiso |
| POST | `/api/compromisos` | Registrar un crédito |
| POST | `/api/compromisos/{folio}/pagar` | Pagar una cuota |
| PUT | `/api/compromisos/{folio}/estado` | Pasar a En revisión al reportar |
| PUT | `/api/compromisos/{folio}/anular` | Anular con motivo |
| GET | `/api/comprobantes?rut=` | Historial de pagos |
| GET | `/api/reportes` | Bandeja del agente |
| POST | `/api/reportes` | Reportar un problema |
| PUT | `/api/reportes/{numero}/tomar` | Tomar un reporte |
| PUT | `/api/reportes/{numero}/resolver` | Resolver un reporte |

## 5. Pasos para ejecutar

1. Abrir **Laragon** y presionar **Iniciar todo** (MySQL en el puerto 3306, usuario `root` sin contraseña).
2. En la carpeta del repositorio de microservicios, hacer doble clic en `encender-todo.bat` y esperar a que las 5 ventanas muestren `Started`.
3. Abrir la carpeta de microservicios en **VS Code**, ir a la pestaña **Puertos**, reenviar el puerto `8080` y cambiar su visibilidad a **Público**. La dirección que aparece es la que usa `RetrofitClient.kt`.
4. Abrir este proyecto en **Android Studio**, esperar la sincronización de Gradle y presionar **Run** en un emulador.
5. Pruebas unitarias: `.\gradlew test`

## 6. APK firmado y llave

Pendiente: se agrega la captura del APK firmado y del archivo `.jks` al generar la versión final.
