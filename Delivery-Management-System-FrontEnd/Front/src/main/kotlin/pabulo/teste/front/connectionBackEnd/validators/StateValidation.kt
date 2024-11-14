package pabulo.teste.front.connectionBackEnd.validators

import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.StateConnection

class StateValidation {

    private val stateConnection = StateConnection(GsonProvider.gson)


    fun verifiesOrderStateExists(orderStateCode: Long): String{


        val state = stateConnection.fetchByOrderStateCode(orderStateCode)
        var result = " "


        if (state != null){

           result = "A situação do pedido de codigo: $orderStateCode já está salva no banco de dados web"


        }


        return result

    }


    fun stateValidadet(orderStateCode: Long): String{

        val state = stateConnection.fetchByOrderStateCode(orderStateCode)
        var result = "Não Sincronizado"


        if (state != null){

            result = "Sincronizado"


        }

        return result


    }


}