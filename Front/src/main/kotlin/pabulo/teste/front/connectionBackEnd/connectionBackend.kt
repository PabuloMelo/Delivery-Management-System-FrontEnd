@file:Suppress("DEPRECATION")

package pabulo.teste.front.connectionBackEnd

import com.google.gson.Gson
import pabulo.teste.front.dtoCoverter.customer.convertLocalCustomerDto
import pabulo.teste.front.resource.customerResouce.CustomerResource
import java.net.HttpURLConnection
import java.net.URL


class ConnectionBackend {

    private val customerResource = CustomerResource()


    private fun postToBack(endPoint: String, jsonData: String): String {

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

    private fun getFromBack(endPoint: String): String {

        val url = URL(endPoint)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        return connection.inputStream.bufferedReader().use { it.readText() }

    }


    fun saveCustomerOnWebDb(customerCode: Int) {

        val customer = customerResource.findCustomerByCodeInLocalDb(customerCode)


        val customerDto = convertLocalCustomerDto(customer!!)

        val customerJson = Gson().toJson(customerDto)

        try {
            val response = postToBack("http://localhost:8080/api/customers/save", customerJson)
            println("response from BackEnd $response")

            println(customerJson)

        } catch (e: Exception) {

            println("erro ao tentar sincronizar com o backEnd ${e.message}")
            println(customerJson)
        }
    }

    fun fetchCustomerOnWebDbByCode(customerCode: Long){

        val endPoint = "http://localhost:8080/api/customers/code/$customerCode"

        try {
            val response = getFromBack(endPoint)

            println("response from Back $response")

        }catch (e: Exception){

            println("erro ao buscar o cliente ${e.message}")


        }

    }

}