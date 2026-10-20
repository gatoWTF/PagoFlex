package com.example.pagoflex.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pagoflex.R

object Notificaciones {

    const val CANAL_AVISOS = "avisos_vencimiento"
    private const val ID_AVISO = 1001

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
