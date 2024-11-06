@file:Suppress("DEPRECATION")

package pabulo.teste.front.connectionBackEnd

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class ConnectionBackend {




     fun postToBack(endPoint: String, jsonData: String): String {

        val url = URL(endPoint)

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "Application/json; utf-8")
        connection.doOutput = true


        connection.outputStream.use {

                os ->
            val input = jsonData.toByteArray()

            os.write(input, 0, input.size)


        }

        return connection.inputStream.bufferedReader().use { it.readText() }

    }


    fun postToBackTest(endPoint: String): String {

        val url = URL(endPoint)

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true


        return connection.inputStream.bufferedReader().use { it.readText() }

    }

     fun getFromBack(endPoint: String): String {

        val url = URL(endPoint)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        return connection.inputStream.bufferedReader().use { it.readText() }

    }


    fun patchToBack(endPoint: String, jsonData: String): String {
        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonData.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(endPoint)
            .patch(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body?.string() ?: ""
        }
    }



}