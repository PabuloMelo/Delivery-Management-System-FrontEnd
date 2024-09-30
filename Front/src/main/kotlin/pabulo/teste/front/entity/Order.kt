package pabulo.teste.front.entity

data class Order(

    val orderCode: Int,
    val customerCode: Int,
    var loadCode: Int,
    val customerName: String,
    val orderType: String,
    val orderStatus: String,
    val invoiceDate: String,
    val purchaseDate: String,
    val orderSellerRca: Int,
    val sellerName: String,
    var orderTrouble: String = " "

    )












