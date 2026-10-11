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

## Fase 2 — Recorrido funcional (esquema)

Mostrar el *happy path* del usuario final explicando mientras navegas:
- **Formularios** (login/registro): validación **desacoplada** en el ViewModel (`Validaciones`, RUT módulo 11), mensajes e íconos por campo.
- **Gestión de estado**: `mutableStateOf` / `StateFlow`; cómo la UI reacciona (pagar → comprobante → la lista se actualiza).
- **Persistencia en vivo**: pagar un compromiso, cerrar y reabrir la app para mostrar que el cambio persiste en Room.
- **Cumplimiento e historial** (indicador de % a tiempo).

## Fase 3 — Recursos nativos y modificación en vivo (esquema)

- **Cámara**: en Perfil, "Tomar foto" → permiso → foto redondeada (explicar permiso `CAMERA` y `ActivityResult`).
- **Notificaciones**: botón de campana en el inicio → permiso `POST_NOTIFICATIONS` → aviso de vencimiento.
- **Modificación en vivo** (ensayar una sencilla y segura), por ejemplo:
  - Agregar una validación nueva a un campo (longitud mínima del nombre en el registro).
  - Cambiar el texto o color de un componente reutilizable.
  - Agregar un `Text` con un dato extra en una tarjeta.

---

## Checklist antes de empezar
- [ ] La app **compila y corre** en el emulador (déjalo abierto).
- [ ] Sé entrar con los 3 roles (usuario final, ejecutivo, agente) y recordar los correos.
- [ ] Puedo explicar una pantalla de principio a fin (UI → ViewModel → Room).
- [ ] Ensayé **una** modificación en vivo.
- [ ] GitHub al día y Trello con tareas repartidas.
- [ ] Repo **congelado** tras la entrega del 25-oct (ningún commit después).
