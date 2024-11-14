package pabulo.teste.front.dtoConverter.order

import pabulo.teste.front.dtos.orders.OrderUpdateDTOtoDb
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OrderUpdateToWeb {

    data class OrderUpdateDtoToWeb(

        val orderCode: Long?,
        val customerCode: Long?,
        val orderType: String?,
        val loadNumber: Long?,
        val status: String?,
        val purchaseDate: LocalDate?,
        val invoicingDate: LocalDate?,
        val orderAddress: String?



    )
fun convertOrderUpdateLocalToWeb(orderUpdateLocal: OrderUpdateDTOtoDb): OrderUpdateDtoToWeb{

    return OrderUpdateDtoToWeb(

        orderCode = orderUpdateLocal.orderCode?.toLong(),
        customerCode = orderUpdateLocal.customerCode?.toLong(),
        orderType = orderUpdateLocal.orderType?.let { adapterStringInEnumDb(it) },
        loadNumber = orderUpdateLocal.loadNumber?.toLong(),
        status = orderUpdateLocal.status?.let { adapterStringInEnumDb(it) },
        purchaseDate = orderUpdateLocal.purchaseDate?.let { convertStringInToLocalDate(it) },
        invoicingDate = orderUpdateLocal.invoicingDate?.let { convertStringInToLocalDate(it) },
        orderAddress = orderUpdateLocal.orderAddress


    )

}
    private fun adapterStringInEnumDb(string: String): String {

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


    private fun convertStringInToLocalDate(dateAtConverter: String): LocalDate {

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateAtConverter, dateFormatter)


        return date

    }


}