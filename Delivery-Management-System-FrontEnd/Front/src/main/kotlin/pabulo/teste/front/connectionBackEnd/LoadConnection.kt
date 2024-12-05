package pabulo.teste.front.connectionBackEnd

import com.google.gson.Gson
import pabulo.teste.front.dtoConverter.load.LoadDtoToWebDb
import pabulo.teste.front.dtoConverter.load.converterLoadLocalToWeb
import pabulo.teste.front.resource.loadResource.LoadResource
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

@Suppress("DEPRECATION")
class LoadConnection(private val gson: Gson) {

    private val loadResource = LoadResource()
    private val connectionBackend = ConnectionBackend()


    fun saveLoadOnWebDb(loadCode: Int) {

        val load = loadResource.findLoadByLoadCode(loadCode)

        val loadDto: LoadDtoToWebDb = converterLoadLocalToWeb(load!!)

        val loadJson = gson.toJson(loadDto)

        try {

            val response = connectionBackend.postToBack("http://localhost:8080/api/loads/save", loadJson)

            println("carregamento $response salvo")

        } catch (e: Exception) {

            println("${e.message}")

        }


    }

    fun fetchLoadByLoadCode(loadCode: Long): Boolean {
        val endPoint = "http://localhost:8080/api/loads/load?loadNumber=$loadCode"
        return try {
            val connection = URL(endPoint).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            // Verifica se o status HTTP é 200 (OK)
            connection.responseCode == HttpURLConnection.HTTP_OK
        } catch (e: IOException) {
            // Log do erro para facilitar depuração
            println("Erro ao verificar carregamento: ${e.message}")
            false
        }
    }

}