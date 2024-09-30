package pabulo.teste.front.dtos.driver

import javafx.beans.property.SimpleStringProperty

data class DriverDto(

    val driverName: SimpleStringProperty,
    val driverImagePath: SimpleStringProperty,

    ) {

    constructor(


        driverName: String,
        driverImagePath: String

    ) : this(

        SimpleStringProperty(driverName),
        SimpleStringProperty(driverImagePath)


    )


}


data class SaveDriverDtoToDb(


    var driverName: String,
    var driverImagePath: String

) {

    fun convertDTO(saveDriverDto: DriverDto) {

        this.driverName = saveDriverDto.driverName.get()
        this.driverImagePath = saveDriverDto.driverImagePath.get()

    }


}