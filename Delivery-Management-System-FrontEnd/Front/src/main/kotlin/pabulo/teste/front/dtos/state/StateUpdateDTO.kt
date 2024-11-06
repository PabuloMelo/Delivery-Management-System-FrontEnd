package pabulo.teste.front.dtos.state

import javafx.beans.property.SimpleStringProperty
import pabulo.teste.front.dtos.customer.getValueOrNull


//classe para receber os inputs de atualização da situação do pedido no banco de dados local


data class StateUpdateDTO(

    val state: SimpleStringProperty?,
    val stateNV1: SimpleStringProperty?,
    val stateNV2: SimpleStringProperty?,
    val description: SimpleStringProperty?,
    val solveDriver: SimpleStringProperty?,
    val solveDate: SimpleStringProperty?,
    val resolve: SimpleStringProperty?,


    ) {

    constructor(


        state: String?,
        stateNV1: String?,
        stateNV2: String?,
        description: String?,
        solveDriver: String?,
        solveDate: String?,
        resolve: String?


    ) : this(


        state?.let { SimpleStringProperty(it) },
        stateNV1?.let { SimpleStringProperty(it) },
        stateNV2?.let { SimpleStringProperty(it) },
        description?.let { SimpleStringProperty(it) },
        solveDriver?.let { SimpleStringProperty(it) },
        solveDate?.let { SimpleStringProperty(it) },
        resolve?.let { SimpleStringProperty(it) }
    )

}


//classe para receber os inputs de atualização da situação do pedido no banco de dados web

data class StateUpdateDTOWebDB(

    val orderNumber: Long?,
    val state: SimpleStringProperty?,
    val stateNV1: SimpleStringProperty?,
    val stateNV2: SimpleStringProperty?,
    val description: SimpleStringProperty?,
    val solveDriver: SimpleStringProperty?,
    val solveDate: SimpleStringProperty?,
    val resolve: SimpleStringProperty?,


    ) {

    constructor(

        orderNumber: Long?,
        state: String?,
        stateNV1: String?,
        stateNV2: String?,
        description: String?,
        solveDriver: String?,
        solveDate: String?,
        resolve: String?


    ) : this(

       orderNumber,
        state?.let { SimpleStringProperty(it) },
        stateNV1?.let { SimpleStringProperty(it) },
        stateNV2?.let { SimpleStringProperty(it) },
        description?.let { SimpleStringProperty(it) },
        solveDriver?.let { SimpleStringProperty(it) },
        solveDate?.let { SimpleStringProperty(it) },
        resolve?.let { SimpleStringProperty(it) }


    )

}

//classe para atualização da situação do pedido no banco de dados web

data class StateUpdateDTOtoWebDB(

    var orderNumber: Long? = null,
    var state: String? = null,
    var stateNV1: String? = null,
    var stateNV2: String? = null,
    var description: String? = null,
    var solveDriver: String? = null,
    var solveDate: String? = null,
    var resolve: String? = null,


    ) {

    fun covertDto(saveStateDTO: StateUpdateDTOWebDB) {

        this.orderNumber = saveStateDTO.orderNumber
        this.state = saveStateDTO.state.getValueOrNull()
        this.stateNV1 = saveStateDTO.stateNV1.getValueOrNull()
        this.stateNV2 = saveStateDTO.stateNV2.getValueOrNull()
        this.description = saveStateDTO.description.getValueOrNull()
        this.solveDriver = saveStateDTO.solveDriver.getValueOrNull()
        this.solveDate = saveStateDTO.solveDate.getValueOrNull()
        this.resolve = saveStateDTO.resolve.getValueOrNull()

    }
}
//classe para atualização da situação do pedido no banco de dados local

data class StateUpdateDTOtoDB(

    var state: String? = null,
    var stateNV1: String? = null,
    var stateNV2: String? = null,
    var description: String? = null,
    var solveDriver: String? = null,
    var solveDate: String? = null,
    var resolve: String? = null,


    ) {

    fun covertDto(saveStateDTO: StateUpdateDTO) {


        this.state = saveStateDTO.state.getValueOrNull()
        this.stateNV1 = saveStateDTO.stateNV1.getValueOrNull()
        this.stateNV2 = saveStateDTO.stateNV2.getValueOrNull()
        this.description = saveStateDTO.description.getValueOrNull()
        this.solveDriver = saveStateDTO.solveDriver.getValueOrNull()
        this.solveDate = saveStateDTO.solveDate.getValueOrNull()
        this.resolve = saveStateDTO.resolve.getValueOrNull()

    }


}

