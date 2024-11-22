@file:Suppress("DEPRECATION")

package pabulo.teste.front.connectionBackEnd

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


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

    fun testConnection(): Int {

        return try {


            val cliente = HttpClient.newBuilder().build()

            val request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/database-status"))
                .GET()
                .build()

            val response = cliente.send(request, HttpResponse.BodyHandlers.ofString())
            if (response.statusCode() == 200) {

                println("Sucesso: ${response.body()} ")

                1

            } else {

                println("Falha: ${response.body()}")

                2


            }

        } catch (e: Exception) {

            println("Erro de Conexão: ${e.message}")

            3

        }


    }

}