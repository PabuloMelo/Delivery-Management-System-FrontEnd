package pabulo.teste.front.connectionBackEnd.validators

import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.OrderConnection

class LoadValidation {

    private val orderConnection = OrderConnection(GsonProvider.gson)

    fun verifiesLoadExists(loadCode: Long): String {

        val orderCode = null
        val customerCode = null
        val customerName = null
        val orderType = null
        val purchaseDateInit = null
        val purchaseDateEnd = null
        val invoiceDateInit = null
        val invoiceDateEnd = null


        val load = orderConnection.fetchOrdersByUserParameters(orderCode,
            customerCode,
            customerName,
            loadCode,
            orderType,
            purchaseDateInit,
            purchaseDateEnd,
            invoiceDateInit,
            invoiceDateEnd)


        var loadExists = " "


        if (!load.isNullOrEmpty()) {

            loadExists = "Já existe um carregamento salvo com o codigo: $loadCode no banco dados web"

        }

        return loadExists

    }

    fun syncLoad(loadCode: Long): String {

        val orderCode = null
        val customerCode = null
        val customerName = null
        val orderType = null
        val purchaseDateInit = null
        val purchaseDateEnd = null
        val invoiceDateInit = null
        val invoiceDateEnd = null


        val loadSync =  orderConnection.fetchOrdersByUserParameters(orderCode,
            customerCode,
            customerName,
            loadCode,
            orderType,
            purchaseDateInit,
            purchaseDateEnd,
            invoiceDateInit,
            invoiceDateEnd)

        var result = "Não Sincronizado"

        if (loadSync != null) {

            result = "Sincronizado"

        }

        return result

    }

}