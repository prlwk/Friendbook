package com.src.book.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

class PhotoCompression {
    private val MAX_FILE_SIZE = 0.8
    fun execute(context: Context, uri: Uri): Uri {
        val file = uri.path?.let { File(it) }
        if (file != null) {
            val fileSize = (file.length()) / (1024 * 1024)
            if (fileSize > MAX_FILE_SIZE) {
                val bitmap = uriToBitmap(context, uri)
                val height = bitmap.height
                val width = bitmap.width
                var scaled = fileSize.toDouble() / MAX_FILE_SIZE
                if (scaled == 0.0) {
                    scaled = 1.0
                }
                val newHeight = (height/scaled).toInt()
                val newWidth = (width/scaled).toInt()
                val newBitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    newHeight,
                    newWidth,
                    true
                )
                return bitmapToUri(context, newBitmap)
            }
        }
        return uri
    }

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    uri
                )
            )
        }
        return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    private fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
        val cachePath = File(context.externalCacheDir, "my_images/")
        cachePath.mkdirs()
        val file = File(cachePath, "1.png")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        val myImageFileUri = Uri.fromFile(file)
        return myImageFileUri
    }
}