package com.example.greetingcard.sources.manganelo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.models.LatestManga
import com.example.greetingcard.requests.RetrofitClient
import kotlinx.coroutines.launch



class MainSearchModel: ViewModel() {

    val mangas = mutableStateOf<List<LatestManga>>(emptyList())
    private val apiService = RetrofitClient.apiService
    val searchQuery = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun fetchMangasSearch(query: String) {
        isLoading.value = true
        errorMessage.value = null
        viewModelScope.launch {
            try{
                val newMangas = apiService.getSearchInfo(query)
                mangas.value = newMangas
            } catch (e:Exception) {
                errorMessage.value = "error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchPopular() {
        isLoading.value = true
        errorMessage.value = null
        viewModelScope.launch {
            try{
                val newMangas = apiService.getPopular()
                mangas.value = newMangas
            } catch (e:Exception) {
                errorMessage.value = "error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchLatest() {
        isLoading.value = true
        errorMessage.value = null
        viewModelScope.launch {
            try{
                val newMangas = apiService.getLatest()
                mangas.value = newMangas
            } catch (e:Exception) {
                errorMessage.value = "error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}

data class SearchCard(
    val cover:String,
    val title:String,
    val manga_url : String
)