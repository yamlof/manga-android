package com.example.greetingcard.requests

import com.example.greetingcard.models.ImageManga
import com.example.greetingcard.models.LatestManga
import com.example.greetingcard.models.MangaInfo

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


object RetrofitClient {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://manga-sp.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

interface ApiService {

   // private var characterCache = mutableMapOf()

    @GET("hello")
    suspend fun getHelloMessage(): String

    @GET("latest")
    suspend fun getLatest(): List<LatestManga>

    @GET("popular")
    suspend fun getPopular(): List<LatestManga>

    @GET("manga_info")
    suspend fun  getMangaInfo(
        @retrofit2.http.Query("mangaInfo") url :String
    ) : MangaInfo

    @GET("chapter")
    suspend fun getChapterInfo(
        @retrofit2.http.Query("chapterUrl") url :String
    ) : List<ImageManga>

    @GET("search")
    suspend fun getSearchInfo(
        @retrofit2.http.Query("mangaString") url :String
    ) : List<LatestManga>
}

