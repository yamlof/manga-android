package com.example.greetingcard.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(
    application: Application,
    private val mangaRepository: MangaRepository
) : AndroidViewModel(application) {

    val allMangas: MutableLiveData<List<Manga>> = MutableLiveData()
    val _allMangas: LiveData<List<Manga>> = allMangas

    init {
        loadAllMangas()
    }

    private fun loadAllMangas() {
        viewModelScope.launch {
            mangaRepository.getAllMangas().collect { mangas ->
                allMangas.postValue(mangas)
            }
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

            loadAllMangas()
        }
    }

    fun updateManga(manga: Manga) {
        viewModelScope.launch {
            mangaRepository.updateManga(manga)
        }
    }

    fun isInLibrary(manga: Manga): Boolean {
        return allMangas.value?.any { it.name == manga.name } == true
    }



    fun deleteManga(name: String) {
        Log.d("MangaVM", "Deleting manga: $name")
        viewModelScope.launch {
            mangaRepository.deleteManga(name)
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

