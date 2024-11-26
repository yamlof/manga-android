package com.example.greetingcard.requests

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

interface ApService {

    @GET("api/")
    fun getHelloMessage(): String

    @POST("api/data")
    fun postData(@Body data: DataModel): Call<ResponseMessage>
}

data class ResponseMessage(val message: String)

data class DataModel(val name: String, val age: Int)