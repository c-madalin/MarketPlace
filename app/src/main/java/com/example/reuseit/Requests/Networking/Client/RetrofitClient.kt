package com.example.reuseit.Requests.Networking.Client

import com.example.reuseit.Requests.Networking.API.ReqApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: ReqApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReqApiService::class.java)
    }
}