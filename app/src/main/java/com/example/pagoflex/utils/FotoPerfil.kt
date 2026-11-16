package com.example.pagoflex.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object FotoPerfil {

    private const val LADO_MINIMO = 512

    fun crearUriParaCamara(context: Context): Uri {
        val archivo = File(context.cacheDir, "camara/foto_camara.jpg")
        archivo.parentFile?.mkdirs()
        return FileProvider.getUriForFile(context, context.packageName + ".fotos", archivo)
    }

    fun leer(context: Context, uri: Uri): Bitmap? = try {
        val resolver = context.contentResolver
        val medidas = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, medidas) }

        var reduccion = 1
        while (medidas.outWidth / (reduccion * 2) >= LADO_MINIMO &&
            medidas.outHeight / (reduccion * 2) >= LADO_MINIMO
        ) {
            reduccion *= 2
        }
        val opciones = BitmapFactory.Options().apply { inSampleSize = reduccion }
        val imagen = resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opciones) }
        val orientacion = resolver.openInputStream(uri)?.use {
            ExifInterface(it).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        } ?: ExifInterface.ORIENTATION_NORMAL

        imagen?.let { enderezar(it, orientacion) }
    } catch (e: Exception) {
        null
    }

    fun guardar(context: Context, rut: String, foto: Bitmap) {
        archivoDe(context, rut).outputStream().use { foto.compress(Bitmap.CompressFormat.JPEG, 90, it) }
    }

    fun cargar(context: Context, rut: String): Bitmap? {
        val archivo = archivoDe(context, rut)
        return if (archivo.exists()) BitmapFactory.decodeFile(archivo.absolutePath) else null
    }

    private fun archivoDe(context: Context, rut: String): File {
        val carpeta = File(context.filesDir, "fotos")
        carpeta.mkdirs()
        val rutClave = rut.trim().uppercase().replace(".", "").replace("-", "")
        return File(carpeta, "perfil_$rutClave.jpg")
    }

    private fun enderezar(imagen: Bitmap, orientacion: Int): Bitmap {
        val matriz = Matrix()
        when (orientacion) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matriz.setRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matriz.setRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matriz.setRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matriz.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matriz.setScale(1f, -1f)
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matriz.setRotate(90f)
                matriz.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matriz.setRotate(270f)
                matriz.postScale(-1f, 1f)
            }
            else -> return imagen
        }
        return Bitmap.createBitmap(imagen, 0, 0, imagen.width, imagen.height, matriz, true)
    }
}
