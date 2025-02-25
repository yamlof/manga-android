package com.example.greetingcard.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greetingcard.requests.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class MangaViewModel(application: Application,private val mangaRepository: MangaRepository) : AndroidViewModel(application) {

    val allMangas: MutableLiveData<List<Manga>> = MutableLiveData()

    init {
        loadAllMangas()
    }

    private fun loadAllMangas() {
        viewModelScope.launch {
            allMangas.postValue(mangaRepository.getAllMangas().first())
        }
    }

    fun getAllManga(): LiveData<List<Manga>> = allMangas

    /*fun getMangasByStatus(status: String): LiveData<List<Manga>> {
        val result = MutableLiveData<List<Manga>>()
        viewModelScope.launch {
            result.postValue(mangaRepository.getMangasByStatus(status))
        }
        return result
    } */

    fun addManga(manga: Manga) = viewModelScope.launch(Dispatchers.IO){
        viewModelScope.launch {
            mangaRepository.insertManga(manga)
            val mangainf = RetrofitClient.apiService.getMangaInfo(manga.mangaUrl)

            val chapterEntities =mangainf.chapters.map{ chapterDto ->
                ChapterRoom(
                    imgTitle = chapterDto.chapterTitle,
                    imgLink = chapterDto.chapterLink,
                    mangaUrl = manga.mangaUrl
                )

            }

            chapterEntities.forEach { chapter ->
                mangaRepository.addChapter(chapter)
            }

            loadAllMangas()
        }
    }

    fun updateManga(manga: Manga) {
        viewModelScope.launch {
            mangaRepository.updateManga(manga)
        }
    }

    fun deleteManga(manga: Manga) {
        viewModelScope.launch {
            mangaRepository.deleteManga(manga)
        }
    }
}

class MangaViewModelFactory(private val mangaRepository: MangaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MangaViewModel::class.java)) {
            return MangaViewModel(application = Application(),mangaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

