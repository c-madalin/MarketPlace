package com.example.reuseit.Requests.Database.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.reuseit.Requests.Database.Entity.ReqPostEntity
import com.example.reuseit.Requests.Database.Entity.ReqUserEntity
import com.example.reuseit.Requests.Database.ReqAppDatabase
import com.example.reuseit.Requests.Models.ReqPostModel
import com.example.reuseit.Requests.Networking.Client.RetrofitClient
import kotlinx.coroutines.launch


class ReqPostViewModel(application: Application): AndroidViewModel(application) {
    private val database = Room.databaseBuilder(
        application.applicationContext,
        ReqAppDatabase::class.java, "my-database"
    ).build()

    val postsToBeShown = MutableLiveData<List<ReqPostModel>>()

    fun fetchDataFromApi() {
        viewModelScope.launch {
            try {
                val users = RetrofitClient.api.getUsers()
                val posts = RetrofitClient.api.getPosts()

                val userEntities = users.map { reqApiUser ->
                    ReqUserEntity(
                        reqUserId = reqApiUser.id,
                        userName = reqApiUser.name,
                        userAddress = "${reqApiUser.address.street}, ${reqApiUser.phone}"
                    )
                }
                val postEntities = posts.map { reqApiPost ->
                    ReqPostEntity(
                        reqPostId = reqApiPost.id,
                        userID = reqApiPost.userId,
                        postTitle = reqApiPost.title,
                        postMessage = reqApiPost.body
                    )
                }

                database.reqUserDAO().deleteAllUsers()
                database.reqPostDAO().deleteAllPosts()
                database.reqUserDAO().insertUsers(userEntities)
                database.reqPostDAO().insertPosts(postEntities)

                loadFromDbAndDisplay()
            } catch (e: Exception) {
                Log.e("API", "Error fetching data from API", e)
            }
        }
    }

    private fun loadFromDbAndDisplay() {
        viewModelScope.launch {
            val allUsers = database.reqUserDAO().getAllUsers()
            val allPosts = database.reqPostDAO().getAllPosts()

            val postModels = allPosts.map { post ->
                val user = allUsers.find { it.reqUserId == post.userID }
                ReqPostModel(
                    user?.userName ?: "Unknown",
                    user?.userAddress ?: "Unknown",
                    post.postTitle,
                    post.postMessage
                )
            }
            postsToBeShown.postValue(postModels)
        }
    }
}