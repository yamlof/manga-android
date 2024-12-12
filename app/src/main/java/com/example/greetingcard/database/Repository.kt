package com.example.greetingcard.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow

class MangaRepository(private val mangaDao: MangaDao) {

    suspend fun insertManga(manga: Manga) {
        mangaDao.insertManga(manga)
    }

    suspend fun updateManga(manga: Manga) {
        mangaDao.update(manga)
    }

    suspend fun deleteManga(manga: Manga) {
        mangaDao.delete(manga)
    }

    /*suspend fun getMangasByStatus(status: String): List<Manga> {
        return mangaDao.getMangasByStatus(status)
    }*/

    suspend fun getAllMangas(): Flow<List<Manga>> {
        return mangaDao.getAllMangas()
    }

}


