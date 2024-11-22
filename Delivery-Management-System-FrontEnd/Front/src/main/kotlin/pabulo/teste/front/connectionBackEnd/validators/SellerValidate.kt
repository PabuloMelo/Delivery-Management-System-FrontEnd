package pabulo.teste.front.connectionBackEnd.validators

import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.SellerConnection

class SellerValidate {

    private val sellerConnection = SellerConnection(GsonProvider.gson)
    fun testSellerExists(sellerCode: Long): Boolean {

        val seller = sellerConnection.fetchSellerByRca(sellerCode)

        return seller != null

    }


}