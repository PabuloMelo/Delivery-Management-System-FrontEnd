package pabulo.teste.front.connectionBackEnd

import com.google.gson.Gson
import pabulo.teste.front.dtoConverter.seller.SellerWebConverter
import pabulo.teste.front.dtoConverter.seller.convertWebSellerToLocal
import pabulo.teste.front.dtoConverter.seller.covertLocalSellerToWebDb
import pabulo.teste.front.entity.Seller
import pabulo.teste.front.resource.sellerResource.SellerResource

class SellerConnection(private val gson: Gson) {

    private val connectionBackend = ConnectionBackend()
    private val sellerResource = SellerResource()


    fun saveSellerOnWebDb(sellerRca: Int) {

        val seller = sellerResource.findSellerInLocalDbByRca(sellerRca)

        val sellerDto = covertLocalSellerToWebDb(seller!!)

        val sellerJson = gson.toJson(sellerDto)

        println(sellerJson)

        try {

            val response = connectionBackend.postToBack("http://localhost:8080/api/seller/saveSeller", sellerJson)
            println("vendedor salvo $response")

        } catch (e: Exception) {

            println("não foi possivel salvar o vendedor ${e.message}")

        }

    }

    fun fetchSellerByRca(sellerRca: Long): Seller? {

        val endPoint = "http://localhost:8080/api/seller/sellerRca/$sellerRca"

        val sellerWeb: SellerWebConverter?
        var seller: Seller? = null

        try {

            val response = connectionBackend.getFromBack(endPoint)

            println("Vendedor encontrado $response")

            sellerWeb = Gson().fromJson(response, SellerWebConverter::class.java)

            seller = convertWebSellerToLocal(sellerWeb)


        } catch (e: Exception) {

            println(e.message)

        }


        return seller


    }

    fun fetchSellerByName(sellerName: String): Seller? {


        val endPoint = "http://localhost:8080/api/seller/sellerName/$sellerName"

        val sellerWeb: SellerWebConverter?
        var seller: Seller? = null

        try {

            val response = connectionBackend.getFromBack(endPoint)

            println("Vendedor encontrado $response")

            sellerWeb = Gson().fromJson(response, SellerWebConverter::class.java)

            seller = convertWebSellerToLocal(sellerWeb)


        } catch (e: Exception) {

            println(e.message)

        }

        return seller

    }

}