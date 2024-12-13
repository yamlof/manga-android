package com.example.greetingcard.sources.manganelo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.database.Manga
import com.example.greetingcard.models.LatestManga
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.http.Query
import com.example.greetingcard.requests.RetrofitClient
import kotlinx.coroutines.flow.StateFlow


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