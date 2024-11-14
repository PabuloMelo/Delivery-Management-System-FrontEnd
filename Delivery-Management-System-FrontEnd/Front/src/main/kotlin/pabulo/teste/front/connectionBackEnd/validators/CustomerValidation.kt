package pabulo.teste.front.connectionBackEnd.validators

import pabulo.teste.front.connectionBackEnd.CustomerConnection

class CustomerValidation {

    private val customerConnection = CustomerConnection()

    fun verifiesCustomerExistsOnDataWeb(customerCode: Long): String{

        val customer = customerConnection.fetchCustomerOnWebDbByCode(customerCode)
        var result = " "

        if (customer != null){

            result = "Já existe um cliente com o codigo $customerCode salvo no banco de dados Web"

        }
        return result
    }

    fun testSyncSuccess(customerCode: Long): String{

        val customer = customerConnection.fetchCustomerOnWebDbByCode(customerCode)
        var result = "Não Sincronizado"

        if (customer != null){

            result = "Sincronizado"

        }
        return result

    }

}