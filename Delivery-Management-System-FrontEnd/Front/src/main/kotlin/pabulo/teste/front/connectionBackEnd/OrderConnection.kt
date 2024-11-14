package pabulo.teste.front.connectionBackEnd

import com.google.gson.Gson
import pabulo.teste.front.dtoConverter.order.*
import pabulo.teste.front.entity.Order
import pabulo.teste.front.resource.orderResource.OrderResource
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OrderConnection(private val gson: Gson) {

    private val orderResource = OrderResource()
    private val connectionBackend = ConnectionBackend()


    fun saveOrderOnDBWeb(localOrderCode: Int) {

        val orderLocal = orderResource.findOrderByCode(localOrderCode)

        val orderDto: OrderWebConverter = covertOrderLocalToWeb(orderLocal!!)

        val orderJson = gson.toJson(orderDto)

        println(orderJson)

        try {

            val response = connectionBackend.postToBack("http://localhost:8080/api/orders/saveOrder", orderJson)
            println("pedido salvo $response")

        } catch (e: Exception) {

            println("não foi possivel salvar o pedido ${e.message}")


        }

    }

    fun fetchOrderByCode(orderCode: Long): Order? {

        val endPoint = "http://localhost:8080/api/orders/$orderCode"


        val orderWeb: WebOrder?
        var order: Order? = null

        try {
            val response = connectionBackend.getFromBack(endPoint)

            println("response $response")

            orderWeb = gson.fromJson(response, WebOrder::class.java)

            order = convertWebOrderToLocalOrder(orderWeb)

        } catch (e: Exception) {

            println(e.message)
        }

        println(order)

        return order
    }


    fun fetchAllOrdersByCustomerCode(customerCode: Long?): List<Order>? {

        val endPoint = "http://localhost:8080/api/orders/customerCode/$customerCode"

        var ordersList: List<Order>? = null


        try {

            val response = connectionBackend.getFromBack(endPoint)

            val ordersWeb = gson.fromJson(response, Array<WebOrder>::class.java).toList()

            ordersList = ordersWeb.map { convertWebOrderToLocalOrder(it) }


        } catch (e: Exception) {

            println(e.message)

        }

        println(ordersList)


        return ordersList

    }

    fun fetchAllOrdersByLoadCode(loadNumber: Long?): List<Order>? {

        val endPoint = "http://localhost:8080/api/loads/load/$loadNumber"

        var ordersList: List<Order>? = null


        try {

            val response = connectionBackend.getFromBack(endPoint)

            val ordersWeb = gson.fromJson(response, Array<WebOrder>::class.java).toList()

            ordersList = ordersWeb.map { convertWebOrderToLocalOrder(it) }


        } catch (e: Exception) {

            println(e.message)

        }

        println(ordersList)


        return ordersList

    }


    fun fetchOrdersByUserParameters(
        orderCode: Long?,
        customerCode: Long?,
        customerName: String?,
        loadCode: Long?,
        orderType: String?,
        purchaseDateInit: LocalDate?,
        purchaseDateEnd: LocalDate?,
        invoiceDateInit: LocalDate?,
        invoiceDateEnd: LocalDate?
    ): List<Order>? {

        val baseEndPoint = "http://localhost:8080/api/orders/searchAllBy"
        val formatter = DateTimeFormatter.ISO_DATE

        val params = mutableListOf<String>()

        orderCode?.let { params.add("orderCode=$it") }

        customerCode?.let { params.add("customerCode=$customerCode") }

        customerName?.let {

            val encodedName = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
            params.add("customerName=$encodedName")
        }

        loadCode?.let { params.add("loadCode=$loadCode") }

        orderType?.let {

            val encodedOrderType = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
            params.add("orderType=$encodedOrderType")

        }

        purchaseDateInit?.let { params.add("purchaseDateInit=${it.format(formatter)}") }

        purchaseDateEnd?.let { params.add("purchaseDateEnd=${it.format(formatter)}") }

        invoiceDateInit?.let { params.add("invoiceDateInit=${it.format(formatter)}") }

        invoiceDateEnd?.let { params.add("invoiceDate=${it.format(formatter)}") }

        val urlEnd = if (params.isNotEmpty()) {

            "$baseEndPoint?${params.joinToString("&")}"

        } else {

            baseEndPoint

        }

        println("teste url $urlEnd")


        var ordersList: List<Order>? = null


        try {

            val response = connectionBackend.getFromBack(urlEnd)

            val ordersWeb = gson.fromJson(response, Array<WebOrder>::class.java).toList()

            ordersList = ordersWeb.map { convertWebOrderToLocalOrder(it) }


        } catch (e: Exception) {

            println(e.message)

        }

        println(ordersList)


        return ordersList
    }


    fun updateAllOrders() {

        val endPoint = "http://localhost:8080/api/orders/updateAll"


        val response = connectionBackend.postToBackTest(endPoint)

        println(response)


    }

    fun updateOrderOnWebDb(orderCode: Long, orderUpdateDto: OrderUpdateToWeb.OrderUpdateDtoToWeb) {

        val endPoint = "http://localhost:8080/api/orders/updateOrder?orderCode=$orderCode"

        println(endPoint)

        val orderUpdateJson = gson.toJson(orderUpdateDto)

        println("JSON gerado $orderUpdateJson")


        (
            try {

                val response = connectionBackend.patchToBack(endPoint, orderUpdateJson)

                println("pedido $response atualizado")

            } catch (e: Exception){

                println(e.message)

            }
        )


    }


}