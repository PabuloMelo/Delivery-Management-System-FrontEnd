package pabulo.teste.front.connectionBackEnd.validators

import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.OrderConnection

class OrderValidation {


    private val orderConnection = OrderConnection(GsonProvider.gson)


    fun verifiesOrderExistsOnWebDb(orderCode: Long): String{

        val order = orderConnection.fetchOrderByCode(orderCode)
        var result = " "

        if (order != null){

            result = "Já existe um pedido salvo no banco de dados com o codigo $orderCode"

        }

        return result
    }

    fun syncOrderDbValidation(orderCode: Long): String{

        val orderToSync = orderConnection.fetchOrderByCode(orderCode)
        var result = "Não Sincronizado"


        if (orderToSync != null){

            result = "Sincronizado"

        }

        return result

    }

}