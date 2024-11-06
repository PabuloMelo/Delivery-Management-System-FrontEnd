package pabulo.teste.front.dtos.load

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

data class LoadDTO(

    val loadCode: SimpleIntegerProperty,
    val loadDriver: SimpleStringProperty,
    val loadDepartureDate: SimpleStringProperty
) {


    constructor(

        loadCode: Int,
        loadDriver: String,
        loadDepartureDate: String

    ) : this(

        SimpleIntegerProperty(loadCode),
        SimpleStringProperty(loadDriver),
        SimpleStringProperty(loadDepartureDate)

    )

}

data class LoadDTOtoDB(

    var loadCode: Int,
    var loadDriver: String,
    var loadDepartureDate: String,
    var loadValidate: String
) {


    fun convertDTO(loadDTO: LoadDTO) {
        this.loadCode = loadDTO.loadCode.get()
        this.loadDriver = loadDTO.loadDriver.get()
        this.loadDepartureDate = loadDTO.loadDepartureDate.get()

    }


}