package com.example.pagoflex.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pagoflex.R

/**
 * Notificaciones locales (recurso nativo). Se usan para los avisos de
 * vencimiento (RF-11). No dependen de red ni de un servidor: la app le pide al
 * sistema que muestre el aviso en el momento que el codigo lo decide.
 */
object Notificaciones {

    const val CANAL_AVISOS = "avisos_vencimiento"
    private const val ID_AVISO = 1001

    // Crea el canal una vez (obligatorio desde Android 8). Si ya existe, no pasa nada.
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL_AVISOS,
                "Avisos de vencimiento",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Recordatorios de compromisos por pagar" }
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(canal)
        }
    }

    // Muestra el aviso. Quien llama ya verifico el permiso POST_NOTIFICATIONS.
    fun mostrarAviso(context: Context, titulo: String, texto: String) {
        val aviso = NotificationCompat.Builder(context, CANAL_AVISOS)
            .setSmallIcon(R.drawable.ic_avisos)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(texto))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(ID_AVISO, aviso)
    }
}
