package com.example.reuseit.Network

import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

object HttpService {

    suspend fun performGetRequest(): String {
        val url = URL("https://jsonplaceholder.typicode.com/users")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val stream = connection.inputStream
        return stream.bufferedReader().use { it.readText() }
    }

    suspend fun performPostRequest(): String {
        val url = URL("https://jsonplaceholder.typicode.com/comments")
        val json = JSONObject().apply {
            put("postId", 1)
            put("name", "John Test")
            put("email", "john@test.com")
            put("body", "This is a test comment.")
        }

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.doOutput = true

        connection.outputStream.use { os: OutputStream ->
            os.write(json.toString().toByteArray())
        }

        return connection.inputStream.bufferedReader().use { it.readText() }
    }
}
