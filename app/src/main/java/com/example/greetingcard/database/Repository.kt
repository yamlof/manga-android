package com.example.greetingcard.database

import com.example.greetingcard.models.ImageManga
import kotlinx.coroutines.flow.Flow

class MangaRepository(
    private val mangaDao: MangaDao,
    private val chapterDao: ChapterDao
) {

    fun getMangaById(mangaId: String): Flow<Manga?> {
        return mangaDao.getMangaById(mangaId)
    }

    suspend fun insertManga(manga: Manga) {
        mangaDao.insertManga(manga)
    }

    fun getChaptersForManga(mangaId: String) = chapterDao.getChaptersForManga(mangaId)

    suspend fun addChapter(chapter: ChapterRoom) {
        chapterDao.insertChapter(chapter)
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


