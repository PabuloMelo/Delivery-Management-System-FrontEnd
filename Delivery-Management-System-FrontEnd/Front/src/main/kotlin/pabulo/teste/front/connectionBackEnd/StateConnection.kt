package pabulo.teste.front.connectionBackEnd

import com.google.gson.Gson
import pabulo.teste.front.dtoConverter.state.StateUpdateToWeb
import pabulo.teste.front.dtoConverter.state.StateWebToLocal
import pabulo.teste.front.dtoConverter.state.convertLocalStateToWeb
import pabulo.teste.front.dtoConverter.state.convertStateWebToLocal
import pabulo.teste.front.dtos.state.StateUpdateDTOtoWebDB
import pabulo.teste.front.entity.State
import pabulo.teste.front.resource.stateResource.StateResource
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StateConnection(private val gson: Gson) {

    private val connection = ConnectionBackend()
    private val stateResource = StateResource()


    fun saveStateOnWebDb(stateCode: Int) {

        val state = stateResource.findOrderStateByCode(stateCode)

        val stateDTO = convertLocalStateToWeb(state!!)

        val stateJson = gson.toJson(stateDTO)

        println(stateJson)

        try {

            val response = connection.postToBack("http://localhost:8080/api/state/saveState", stateJson)
            println("pedido salvo $response")

        } catch (e: Exception) {

            println("não foi possivel salvar o pedido ${e.message}")

        }

    }


    fun fetchByOrderStateCode(orderStateCode: Long): State? {

        val endPoint = "http://localhost:8080/api/state/orderNumber/$orderStateCode"

        println(endPoint)

        val webState: StateWebToLocal?
        var state: State? = null

        try {

            val response = connection.getFromBack(endPoint)

            println(" pedido encontrado $response")

            webState = gson.fromJson(response, StateWebToLocal::class.java)

            state = convertStateWebToLocal(webState)

        } catch (e: Exception) {

            println(e.message)

        }

        return state

    }


    fun fetchByUserParameter(

        orderCode: Long?,
        customerCode: Long?,
        customerName: String?,
        stateInit: String?,
        stateFirstLevel: String?,
        stateSecondLevel: String?,
        resolve: String?,
        driver: String?,
        invoiceDateInt: LocalDate?,
        invoiceDateEnd: LocalDate?,
        purchaseDateInit: LocalDate?,
        purchaseDateEnd: LocalDate?

    ): List<State>? {

        val baseEndPoint = "http://localhost:8080/api/state/findAllBy/"

        val formatter = DateTimeFormatter.ISO_DATE

        val params = mutableListOf<String>()


        orderCode?.let { params.add("orderCode=$it") }

        customerCode?.let { params.add("customerCode=$it") }

        customerName?.let {

            val encodedName = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
            params.add("customerName=$encodedName")
        }


        stateInit?.let {

            val encodedStateInit = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())

            params.add("stateInit=$encodedStateInit")

        }

        stateFirstLevel?.let {

            val encodedFirstLevel = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())

            params.add("stateFirstLevel=$encodedFirstLevel")

        }

        stateSecondLevel?.let {

            val encodedSecondLevel = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())

            params.add("stateSecondLevel=$encodedSecondLevel")

        }

        resolve?.let {

            val encodedResolve = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())

            params.add("resolve=$encodedResolve")

        }

        driver?.let {

            val encodedDriver = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())

            params.add("driver=$encodedDriver")

        }

        invoiceDateInt?.let { params.add("invoiceDateInt=${it.format(formatter)}") }

        invoiceDateEnd?.let { params.add("invoiceDateEnd=${it.format(formatter)}") }

        purchaseDateInit?.let { params.add("purchaseDateInit=${it.format(formatter)}") }

        purchaseDateEnd?.let { params.add("purchaseDateEnd=${it.format(formatter)}") }

        val urlEnd = if (params.isNotEmpty()) {

            "$baseEndPoint?${params.joinToString("&")}"


        } else {

            baseEndPoint

        }

        println("teste url $urlEnd")


        var stateList: List<State>? = null


        try {

            val response = connection.getFromBack(urlEnd)

            val statesWeb = gson.fromJson(response, Array<StateWebToLocal>::class.java).toList()

            stateList = statesWeb.map {

                convertStateWebToLocal(it)
            }


        } catch (e: Exception) {

            println(e.message)

        }

        println(stateList)


        return stateList


    }


    fun updateOrderStateOnWebDb(originalStateOrderCode: Long, stateUpdateDto: StateUpdateDTOtoWebDB) {

        val endPoint = "http://localhost:8080/api/state/updateState?orderNumber=$originalStateOrderCode"

        val converter = StateUpdateToWeb()

        println(endPoint)

        val stateDto = converter.convertLocalStateDtoToWeb(stateUpdateDto)

        val stateUpdateJson = gson.toJson(stateDto)

        println("Json gerado $stateUpdateJson")

        try {

            val response = connection.patchToBack(endPoint, stateUpdateJson)

            println(" pedido $response atualizado")

        } catch (e: Exception) {

            println(e.message)

        }

    }

}