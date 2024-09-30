package pabulo.teste.front.dtos.state

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

data class StateSaveDto(

    val orderCodeState: SimpleIntegerProperty,
    val customerCode: Int?,
    val customerName: String?,
    val state: SimpleStringProperty,
    val stateNV1: SimpleStringProperty,
    val stateNV2: SimpleStringProperty,
    val description: SimpleStringProperty,
    val invoiceDate: String,
    val purchaseDate: String,
    val solveDriver: SimpleStringProperty?,
    val solveDate: SimpleStringProperty?,
    val resolve: SimpleStringProperty,



    ) {

    constructor(


        orderCodeState: Int,
        customerCode: Int?,
        customerName: String?,
        state: String,
        stateNV1: String,
        stateNV2: String,
        description: String,
        invoiceDate: String,
        purchaseDate: String,
        solveDriver: String,
        solveDate: String,
        resolve: String,



    ) : this(


        SimpleIntegerProperty(orderCodeState),
        customerCode,
        customerName,
        SimpleStringProperty(state),
        SimpleStringProperty(stateNV1),
        SimpleStringProperty(stateNV2),
        SimpleStringProperty(description),
        invoiceDate,
        purchaseDate,
        SimpleStringProperty(solveDriver),
        SimpleStringProperty(solveDate),
        SimpleStringProperty(resolve),



    )

}

data class SaveStateDTOtoDB(

    var orderCodeState: Int,
    var customerCode: Int?,
    var customerName: String?,
    var state: String,
    var stateNV1: String,
    var stateNV2: String,
    var description: String,
    var invoiceDate: String,
    var purchaseDate: String,
    var solveDriver: String?,
    var solveDate: String?,
    var resolve: String,



) {

    fun covertDto(saveStateDTO: StateSaveDto) {

        this.orderCodeState = saveStateDTO.orderCodeState.get()
        this.customerCode = saveStateDTO.customerCode!!
        this.customerName = saveStateDTO.customerName!!
        this.state = saveStateDTO.state.get()
        this.stateNV1 = saveStateDTO.stateNV1.get()
        this.stateNV2 = saveStateDTO.stateNV2.get()
        this.description = saveStateDTO.description.get()
        this.invoiceDate = saveStateDTO.invoiceDate
        this.purchaseDate = saveStateDTO.purchaseDate
        this.solveDriver = saveStateDTO.solveDriver?.get()!!
        this.solveDate = saveStateDTO.solveDate?.get()!!
        this.resolve = saveStateDTO.resolve.get()


    }


}