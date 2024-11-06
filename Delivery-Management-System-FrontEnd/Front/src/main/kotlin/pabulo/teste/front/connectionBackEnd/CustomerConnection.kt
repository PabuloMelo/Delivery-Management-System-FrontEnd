package pabulo.teste.front.connectionBackEnd

import com.google.gson.Gson
import pabulo.teste.front.dtoConverter.customer.CustomerDtoToWebDb
import pabulo.teste.front.dtoConverter.customer.CustomerUpdateToWeb
import pabulo.teste.front.dtoConverter.customer.convertCustomerWebDb
import pabulo.teste.front.dtoConverter.customer.convertLocalCustomerDto
import pabulo.teste.front.entity.Customer
import pabulo.teste.front.resource.customerResouce.CustomerResource

class CustomerConnection() {

    private val connection = ConnectionBackend()
    private val customerResource = CustomerResource()


    fun saveCustomerOnWebDb(customerCode: Int) {

        val customer = customerResource.findCustomerByCodeInLocalDb(customerCode)


        val customerDto = convertLocalCustomerDto(customer!!)

        val customerJson = Gson().toJson(customerDto)

        try {
            val response = connection.postToBack("http://localhost:8080/api/customers/save", customerJson)
            println("response from BackEnd $response")

            println(customerJson)

        } catch (e: Exception) {

            println("erro ao tentar sincronizar com o backEnd ${e.message}")
            println(customerJson)
        }
    }

    fun fetchCustomerOnWebDbByCode(customerCode: Long): Customer? {

        val endPoint = "http://localhost:8080/api/customers/code/$customerCode"


        val customerWeb: CustomerDtoToWebDb?
        var customer: Customer? = null


        try {
            val response = connection.getFromBack(endPoint)

            println("response from Back $response")

            customerWeb = Gson().fromJson(response, CustomerDtoToWebDb::class.java)

            customer = convertCustomerWebDb(customerWeb)

        } catch (e: Exception) {

            println("erro ao buscar o cliente ${e.message}")

        }


        return customer

    }

    fun fetchCustomerOnWebDbByName(customerName: String): List<Customer>? {

        val endPoint = "http://localhost:8080/api/customers/name/$customerName"


        var customer: List<Customer>? = null


        try {
            val response = connection.getFromBack(endPoint)

            println("response from Back $response")

            val customerListType = object : com.google.gson.reflect.TypeToken<List<CustomerDtoToWebDb>>() {}.type
            val customerWebList: List<CustomerDtoToWebDb> = Gson().fromJson(response, customerListType)

            customer = customerWebList.map { convertCustomerWebDb(it) }


        } catch (e: Exception) {

            println("erro ao buscar o cliente ${e.message}")

        }

        return customer

    }

    fun updateCustomerOnDbWeb(customerCode: Long,customerUpdateDto: CustomerUpdateToWeb) {

        val endPoint = "http://localhost:8080/api/customers/updateCustomer?customerCode=$customerCode"


        val customerUpdateJson = Gson().toJson(customerUpdateDto)

        try {
            val response = connection.patchToBack(endPoint,customerUpdateJson)

            println(response)


        }catch (e: Exception){

            println("Não foi possivel atualizar o cliente ${e.message}")

        }

    }

}