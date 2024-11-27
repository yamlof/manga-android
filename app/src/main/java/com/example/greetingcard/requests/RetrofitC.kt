package com.example.greetingcard.requests

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


object RetrofitClient {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://395e-194-66-243-12.ngrok-free.app") // Replace with your backend URL if deployed
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)


}
interface ApiService {
    @GET("hello")
    suspend fun getHelloMessage(): String

    @GET("latest")
    suspend fun getLatest(): List<Map<String,String>>
}

