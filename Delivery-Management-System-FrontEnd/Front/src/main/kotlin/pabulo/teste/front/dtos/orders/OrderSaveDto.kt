package pabulo.teste.front.dtos.orders

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

data class OrderSaveDto(

    val orderCode: SimpleIntegerProperty,
    val customerCode: SimpleIntegerProperty,
    //val customerRegistered:SimpleStringProperty,
    val customerName: SimpleStringProperty,
    val loadNumber: SimpleIntegerProperty? = null,
    val status: SimpleStringProperty,
    val orderType: SimpleStringProperty,
    val purchaseDate: SimpleStringProperty,
    val invoicingDate: SimpleStringProperty,
    val sellerRCA: SimpleIntegerProperty,
    val sellerName: String,
    val orderAddress: SimpleStringProperty
    //val daysUntilDelivery: SimpleIntegerProperty

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
        orderAddress: String

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
        sellerName,
        SimpleStringProperty(orderAddress)


    )

}

data class SaverOrderDTOtoDb(

    var orderCode: Int,
    var customerCode: Int,
    var customerName: String,
    var loadNumber: Int,
    var status: String,
    var orderType: String,
    var purchaseDate: String,
    var invoicingDate: String,
    var sellerRCA: Int,
    var sellerName: String,
    var orderAddress: String

) {

    fun convertDTO(saveOrderSaveDto: OrderSaveDto) {

        this.orderCode = saveOrderSaveDto.orderCode.get()
        this.customerCode = saveOrderSaveDto.customerCode.get()
        this.customerName = saveOrderSaveDto.customerName.get()
        this.loadNumber = (saveOrderSaveDto.loadNumber?.get()!!)
        this.status = saveOrderSaveDto.status.get()
        this.orderType = saveOrderSaveDto.orderType.get()
        this.purchaseDate = saveOrderSaveDto.purchaseDate.get()
        this.invoicingDate = saveOrderSaveDto.invoicingDate.get()
        this.sellerName = saveOrderSaveDto.sellerName
        this.orderAddress = saveOrderSaveDto.orderAddress.get()


    }

}


















