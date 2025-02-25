package com.example.greetingcard.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ImageCacheManager(private val context : Context) {

    private val cacheDir = context.cacheDir

    private val storageDir = File(context.filesDir,"manga_covers")

    init {
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
    }

    suspend fun saveCoverImage(manga : String,bitmap : Bitmap) :String {
        return withContext(Dispatchers.IO) {
            val file = File(storageDir, "$manga.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG,85,out)
            }
            file.absolutePath
        }
    }
    
    fun loadCoverImage(path:String) : Bitmap? {
        val file = File(path)
        return if (file.exists()) {
            BitmapFactory.decodeFile(path)
        } else {
            null
        }
    }
    
    fun deleteCoverImage(manga: String) {
        val file = File(storageDir,"$manga.jpg")
        if (file.exists()) {
            file.delete()
        }
    }
}