package com.example.reuseit.Requests.Networking.API

import com.example.reuseit.Requests.Networking.Models.ReqApiPost
import com.example.reuseit.Requests.Networking.Models.ReqApiUser
import retrofit2.http.GET

interface ReqApiService {
    @GET("users")
    suspend fun getUsers(): List<ReqApiUser>

    @GET("posts")
    suspend fun getPosts(): List<ReqApiPost>
}