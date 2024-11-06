package pabulo.teste.front.connectionBackEnd

import com.google.gson.Gson
import pabulo.teste.front.dtoConverter.seller.covertLocalSellerToWebDb
import pabulo.teste.front.resource.sellerResource.SellerResource

class SellerConnection(private val gson: Gson) {

    private val connectionBackend = ConnectionBackend()
    private val sellerResource = SellerResource()


    fun saveSellerOnWebDb(sellerRca: Int){

        val seller = sellerResource.findSellerInLocalDbByRca(sellerRca)

        val sellerDto = covertLocalSellerToWebDb(seller!!)

        val sellerJson = gson.toJson(sellerDto)

        println(sellerJson)

        try {

            val response = connectionBackend.postToBack("http://localhost:8080/api/seller/saveSeller", sellerJson)
            println("pedido salvo $response")

        }catch (e: Exception){

            println("não foi possivel salvar o pedido ${e.message}")

        }

    }


}