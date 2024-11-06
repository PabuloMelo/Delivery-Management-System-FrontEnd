package pabulo.teste.front.dtoConverter.order

import pabulo.teste.front.entity.Order
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class OrderWebConverter(

    val orderCode: Long,
    val customerCode: Long,
    val loadNumber: Long,
    val status: String,
    val orderType: String,
    val purchaseDate: LocalDate,
    val invoicingDate: LocalDate?,
    val daysUntilDelivery: Int,
    val orderFutureDelState: String,
)

data class WebOrder(

    val orderCode: Long,
    val customer: Long,
    val loadNumber: Long,
    val customerName: String,
    val orderType: String,
    val status: String,
    val purchaseDate: LocalDate,
    val invoicingDate: LocalDate?,
    val orderRca: Long,
    val sellerName: String?,
    val daysUntilDelivery: Int,
    val orderFutureDelState: String,

    )


fun covertOrderLocalToWeb(localOrder: Order): OrderWebConverter {

    return OrderWebConverter(

        orderCode = localOrder.orderCode.toLong(),
        customerCode = localOrder.customerCode.toLong(),
        loadNumber = localOrder.loadCode.toLong(),
        status = adapterStringInEnumDb(localOrder.orderStatus),
        orderType = adapterStringInEnumDb(localOrder.orderType),
        purchaseDate = convertStringInToLocalDate(localOrder.purchaseDate),
        invoicingDate = convertStringInToLocalDate(localOrder.invoiceDate),
        daysUntilDelivery = localOrder.daysUntilDelivery,
        orderFutureDelState = localOrder.orderFutureDelState

    )

}

fun convertStringInToLocalDate(dateAtConverter: String): LocalDate {

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateAtConverter, dateFormatter)


    return date

}

fun adapterStringInEnumDb(string: String): String {

    return string.trim().uppercase()
        .replace("Ç", "C")
        .replace("Ã", "A")
        .replace("Õ", "O")
        .replace("Á", "A")
        .replace("À", "A")
        .replace("É", "E")
        .replace("Í", "I")
        .replace("Ó", "O")
        .replace("Ú", "U")
        .replace("Ê", "E")
        .replace("Ô", "O")
        .replace("Â", "A")
        .replace(" ", "_")
}

fun convertWebOrderToLocalOrder(webOrder: WebOrder): Order {


    return Order(

        orderCode = webOrder.orderCode.toInt(),
        customerCode = webOrder.customer.toInt(),
        loadCode = webOrder.loadNumber.toInt(),
        customerName = webOrder.customerName,
        orderType = adapterStringWebToLocal (webOrder.orderType),
        orderStatus = adapterStringWebToLocal (webOrder.status),
        invoiceDate = webOrder.invoicingDate.toString().replace("-", "/"),
        purchaseDate = webOrder.purchaseDate.toString().replace("-", "/"),
        orderSellerRca = webOrder.orderRca.toInt(),
        sellerName = "Vendedor Não Cadastrado No Servidor Web",
        orderTrouble = " ",
        daysUntilDelivery = webOrder.daysUntilDelivery,
        orderFutureDelState = adapterStringWebToLocal (webOrder.orderFutureDelState),

        )
}

fun adapterStringWebToLocal(string: String): String {

    return string.replace("_", " ")

}