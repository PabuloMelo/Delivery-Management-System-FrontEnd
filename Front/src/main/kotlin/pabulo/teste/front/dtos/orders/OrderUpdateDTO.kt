package pabulo.teste.front.dtos.orders

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import pabulo.teste.front.dtos.customer.getValueOrNull


fun SimpleIntegerProperty?.getValueOrNull(): Int? {

    return this?.takeIf { it.value != null }?.get()

}

fun SimpleStringProperty?.getValueOrNull(): String? {

    return this?.takeIf { it.get().isNotBlank() }?.get()

}

data class OrderUpdateDTO(

    val orderCode: SimpleIntegerProperty?,
    val customerCode: SimpleIntegerProperty?,
    val customerName: SimpleStringProperty?,
    val loadNumber: SimpleIntegerProperty?,
    val status: SimpleStringProperty?,
    val orderType: SimpleStringProperty?,
    val purchaseDate: SimpleStringProperty?,
    val invoicingDate: SimpleStringProperty?,
    val sellerRCA: SimpleIntegerProperty?,
    val sellerName: String?,

    ) {

    constructor(

        orderCode: Int,
        customerCode: Int,
        customerName: String,
        loadNumber: Int,
        status: String,
        orderType: String,
        purchaseDate: String,
        invoicingDate: String,
        sellerRCA: Int,
        sellerName: String,
    ) : this(

        SimpleIntegerProperty(orderCode),
        SimpleIntegerProperty(customerCode),
        SimpleStringProperty(customerName),
        SimpleIntegerProperty(loadNumber),
        SimpleStringProperty(status),
        SimpleStringProperty(orderType),
        SimpleStringProperty(purchaseDate),
        SimpleStringProperty(invoicingDate),
        SimpleIntegerProperty(sellerRCA),
        sellerName


    )


}

data class OrderUpdateDTOtoDb(

    var orderCode: Int? = null,
    var customerCode: Int? = null,
    var customerName: String? = null,
    var loadNumber: Int? = null,
    var status: String? = null,
    var orderType: String? = null,
    var purchaseDate: String? = null,
    var invoicingDate: String? = null,
    var sellerRCA: Int? = null,
    var sellerName: String? = null,
) {


    fun convertDTOtoDB(orderDto: OrderUpdateDTO) {

        this.orderCode = orderDto.orderCode.getValueOrNull()
        this.customerCode = orderDto.customerCode.getValueOrNull()
        this.customerName = orderDto.customerName.getValueOrNull()
        this.loadNumber = orderDto.loadNumber.getValueOrNull()
        this.status = orderDto.status.getValueOrNull()
        this.orderType = orderDto.orderType.getValueOrNull()
        this.purchaseDate = orderDto.purchaseDate.getValueOrNull()
        this.invoicingDate = orderDto.invoicingDate.getValueOrNull()
        this.sellerRCA = orderDto.sellerRCA.getValueOrNull()
        this.sellerName = orderDto.sellerName


    }


}