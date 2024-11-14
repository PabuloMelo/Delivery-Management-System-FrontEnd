package pabulo.teste.front.dtos.address

import javafx.beans.property.SimpleStringProperty

data class SaveAddressView(

    val district: SimpleStringProperty,
    val city: SimpleStringProperty

) {

    constructor(

        district: String,
        city: String,

    ) : this(


        SimpleStringProperty(district),
        SimpleStringProperty(city)

    )

}

data class SaveAddressDTO(

    var district: String,
    var city: String

){

   fun convertSaveAddressViewToDTO(saveAddressView: SaveAddressView){


       this.city = saveAddressView.city.get()
       this.district = saveAddressView.district.get()


   }

}