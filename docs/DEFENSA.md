# Guía de defensa — PagoFlex (DSY1105, P3)

Defensa **individual**, ~15 min. Reglas de oro:
- **La app debe ejecutar.** Si no corre, los ítems posteriores al error = 0. Déjala compilada y el emulador abierto ANTES de empezar.
- El profe puede pedirte **modificar el código en vivo** (agregar una validación, un campo, un componente).
- Habla con seguridad y muestra mientras explicas.

El orden de la defensa sigue los Indicadores de Evaluación (IE). Esta guía detalla la **Fase 1** (apertura); las fases 2 y 3 quedan como esquema para preparar después.

---

## Fase 1 — Apertura: entorno, arquitectura y responsabilidades

Cubre: configuración del entorno · organización del proyecto · responsabilidades de cada capa · justificación de MVVM y herramientas (IE 2.3.2, 2.1.2).

### 1. Cómo se configuró el entorno (30–45 s)
> "El proyecto se abre con Android Studio. Usa el JDK que trae el propio Android Studio (JBR 25) y el SDK de Android. La ruta del SDK va en `local.properties` (no se versiona). Para correrla elijo un emulador con **Android 15 (API 35)** y presiono Run; también funciona en cualquier dispositivo con **Android 7.0 (API 24) o superior**."

Mostrar: el emulador ya corriendo con la app abierta.

### 2. Organización del proyecto (45–60 s)
Abrir el panel de proyecto y recorrer los paquetes:
> "Está organizado en capas siguiendo **MVVM**: `model` tiene los modelos y enums de dominio; `data` la persistencia con **Room** en `data/local` y el `DerivadorDeRol`; `viewmodel` la lógica y el estado; `ui` las pantallas, componentes reutilizables, estilos, tema y navegación; y `utils` las utilidades como formato de moneda, validaciones y notificaciones."

### 3. Responsabilidades de cada capa (60–90 s)
Elegir un ejemplo concreto (el flujo del usuario final) y explicar quién hace qué:
- **`ui/screens/InicioScreen`** → solo dibuja el estado y avisa de las acciones (no tiene lógica).
- **`viewmodel/CompromisosViewModel`** → calcula el total por pagar, el próximo vencimiento, ejecuta el pago; expone el estado con `mutableStateOf`.
- **`data/local`** (Room: `PagoFlexDatabase`, DAOs, entidades) → guarda y recupera los datos; se leen con **Flow/Coroutines**.
- **`DatosSemilla`** → precarga los datos de ejemplo del Anexo 9.

> "La pantalla no sabe de dónde salen los datos; solo pide el estado al ViewModel. El ViewModel no sabe de Compose; solo expone estado y recibe acciones. Eso es lo que hace el código mantenible y testeable."

### 4. Justificar herramientas (20–30 s)
> "Uso **GitHub** para el control de versiones con commits descriptivos, y **Trello** para repartir y seguir las tareas. La arquitectura MVVM + Room permite que el prototipo crezca sin acoplar la interfaz a los datos."

### Posibles preguntas (Fase 1)
- *¿Por qué MVVM?* → separa responsabilidades, facilita pruebas y mantenimiento; la UI es declarativa y reacciona al estado.
- *¿Dónde está la persistencia?* → Room en `data/local`; los DAOs exponen `Flow`, y los ViewModels leen/escriben en `viewModelScope`.
- *¿Cómo se asigna el rol sin login real?* → `DerivadorDeRol.rolPorCorreo()` deduce el rol del dominio del correo; es una función pura con prueba unitaria.

---

## Fase 2 — Recorrido funcional (detalle)

Cubre: formularios y validación · diseño visual y navegación · gestión de estado · persistencia en vivo (IE 2.1.1, 2.1.2, 2.2.2, 2.3.1). Narra mientras navegas.

### 2.1 Formularios y validación (60–90 s)
Mostrar el **registro** y provocar errores a propósito:
> "Los formularios no validan en la propia pantalla: la pantalla solo dibuja. La validación vive en el **ViewModel** (`RegistroViewModel`) usando `utils/Validaciones`. Por ejemplo, el **RUT se valida por módulo 11** y el correo por formato; si está mal, el ViewModel expone el mensaje de error y el campo lo muestra con su ícono."

Mostrar: escribir un RUT inválido → aparece "RUT no válido"; corregirlo → desaparece. Esto demuestra validación **desacoplada** y en vivo.

### 2.2 Diseño visual y navegación (30–45 s)
> "La navegación es un **NavHost** (`MainActivity`): login/registro como entrada y un home por rol. El tema (claro/oscuro) y los componentes reutilizables (`BotonPrincipal`, `CampoTexto`, `FilaCompromiso`) dan consistencia. Las transiciones entre pantallas están animadas."

Mostrar: moverte entre Inicio → Detalle → Comprobante y volver; cambiar el tema en Configuración.

### 2.3 Gestión de estado (30–45 s)
> "El estado se maneja con `mutableStateOf` en los ViewModels y, para Room, con `Flow`/`StateFlow`. La UI es **reactiva**: cuando cambia el estado, Compose vuelve a dibujar solo lo necesario."

Mostrar: pagar un compromiso → el total por pagar y la lista se actualizan solos.

### 2.4 Persistencia en vivo (45–60 s) — clave (IE 2.3.1)
> "La persistencia es **Room/SQLite**. Lo demuestro: pago este compromiso, se genera el comprobante… y ahora **cierro la app y la vuelvo a abrir**: el compromiso sigue pagado, porque se guardó en la base, no en memoria."

Mostrar: pagar → cerrar app (no solo minimizar) → reabrir → el cambio persiste. (El indicador de **cumplimiento** en Historial refuerza este punto.)

## Fase 3 — Recursos nativos y modificación en vivo (detalle)

Cubre: recursos nativos y su presentación · incorporar/modificar en vivo (IE 2.2.1, 2.4.1, 2.4.2). Es la parte de mayor peso en la defensa.

### 3.1 Recursos nativos (60–90 s)
- **Cámara** (Perfil → "Tomar foto"):
  > "Declaro el permiso `CAMERA` en el manifest y lo pido en runtime con `ActivityResultContracts`. Al conceder, abro la cámara con `TakePicturePreview`, guardo la foto en la sesión y la muestro **recortada en un círculo** en el avatar."
- **Notificaciones** (Inicio → campana):
  > "Declaro `POST_NOTIFICATIONS`, lo pido en runtime (obligatorio desde Android 13), creo un canal y muestro una notificación con el **próximo vencimiento y el total** (RF-11, el aviso de hábito)."

Mostrar ambos en vivo y señalar cómo se gestionó el permiso y cómo se presenta el resultado en la UI.

### 3.2 Modificación en vivo (lo que más vale)
Ante la petición del profe, mantén la calma: edita, explica lo que haces y vuelve a correr. Ensaya **una** de estas (de más segura a más vistosa):

**A) Agregar una validación a un campo (recomendada).** En `RegistroViewModel.crearCuenta()`, cambia la validación del nombre para exigir un mínimo de caracteres:
```kotlin
errorNombre = when {
    nombre.isBlank() -> "Ingresa tu nombre"
    nombre.trim().length < 3 -> "El nombre es muy corto"
    else -> null
}
```
Explica: "La regla vive en el ViewModel; la pantalla solo refleja el error." Corre, escribe "Ab" → aparece el mensaje. (Cubre IE 2.1.1 al máximo: modificar la validación en vivo.)

**B) Cambiar un componente reutilizable.** En `ui/components/` ajusta el texto o un color de un botón/etiqueta y muestra que el cambio se propaga a todas las pantallas que lo usan (demuestra modularidad, IE 2.2.1).

**C) Agregar un dato a una tarjeta.** En una pantalla, añade un `Text(...)` con un campo extra (por ejemplo el folio en una fila) para mostrar que sabes dónde tocar la UI.

> Consejo: ten decidido de antemano **cuál** harás y **en qué archivo**, y practícala 2–3 veces hasta que salga sin dudar.

---

## Checklist antes de empezar
- [ ] La app **compila y corre** en el emulador (déjalo abierto).
- [ ] Sé entrar con los 3 roles (usuario final, ejecutivo, agente) y recordar los correos.
- [ ] Puedo explicar una pantalla de principio a fin (UI → ViewModel → Room).
- [ ] Ensayé **una** modificación en vivo.
- [ ] GitHub al día y Trello con tareas repartidas.
- [ ] Repo **congelado** tras la entrega del 25-oct (ningún commit después).
