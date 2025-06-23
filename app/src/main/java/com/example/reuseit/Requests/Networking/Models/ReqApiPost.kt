package com.example.reuseit.Requests.Networking.Models

data class ReqApiPost(
    val userId: Long,
    val id: Long,
    val title: String,
    val body: String
)
