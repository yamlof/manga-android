package com.example.greetingcard.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaRepository @Inject constructor(
    private val mangaDao: MangaDao
) {

    suspend fun insertManga(manga: Manga) {
        mangaDao.insertManga(manga)
    }

    suspend fun updateManga(manga: Manga) {
        mangaDao.update(manga)
    }

    suspend fun deleteManga(name: String) {
        mangaDao.delete(name)
    }

    /*suspend fun getMangasByStatus(status: String): List<Manga> {
        return mangaDao.getMangasByStatus(status)
    }*/

    suspend fun getAllMangas(): Flow<List<Manga>> {
        return mangaDao.getAllMangas()
    }

}


