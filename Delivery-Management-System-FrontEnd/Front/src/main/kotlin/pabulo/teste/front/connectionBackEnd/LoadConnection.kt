package pabulo.teste.front.connectionBackEnd

import com.google.gson.Gson
import pabulo.teste.front.dtoConverter.load.LoadDtoToWebDb
import pabulo.teste.front.dtoConverter.load.converterLoadLocalToWeb
import pabulo.teste.front.resource.loadResource.LoadResource

class LoadConnection(private val gson: Gson) {

    private val loadResource = LoadResource()
    private val connectionBackend = ConnectionBackend()


    fun saveLoadOnWebDb(loadCode: Int){

        val load = loadResource.findLoadByLoadCode(loadCode)

        val loadDto: LoadDtoToWebDb = converterLoadLocalToWeb(load!!)

        val loadJson = gson.toJson(loadDto)

        try {

            val response = connectionBackend.postToBack("http://localhost:8080/api/loads/save",loadJson)

            println("carregamento $response salvo")

        }catch (e: Exception){

            println("${e.message}")

        }


    }

}