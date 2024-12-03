package com.example.greetingcard.requests

import androidx.room.Query
import com.example.greetingcard.items.Manga
import com.example.greetingcard.models.LatestManga
import com.example.greetingcard.models.MangaInfo

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


object RetrofitClient {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://395e-194-66-243-12.ngrok-free.app")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

interface ApiService {
    @GET("hello")
    suspend fun getHelloMessage(): String

    @GET("latest")
    suspend fun getLatest(): List<LatestManga>

    @GET("manga_info")
    suspend fun  getMangaInfo(
        @retrofit2.http.Query("mangaInfo") url :String
    ) : MangaInfo

    @GET("chapterInfo")
    suspend fun getChapterInfo(
        @retrofit2.http.Query("chapter") url :String
    )
}

