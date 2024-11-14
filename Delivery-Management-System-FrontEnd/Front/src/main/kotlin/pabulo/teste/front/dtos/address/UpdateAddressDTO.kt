package pabulo.teste.front.dtos.address

import javafx.beans.property.SimpleStringProperty


fun SimpleStringProperty?.getValueOrNull(): String? {
    return this?.takeIf { it.get().isNotBlank() }?.get()
}

data class UpdateAddressView(

val city: SimpleStringProperty?,
val district: SimpleStringProperty?

)

data class UpdateAddressDTO(

    var city: String? = null,
    var district: String? = null

){
    fun convertViewToDTO(updateAddressView: UpdateAddressView){

        this.city = updateAddressView.city.getValueOrNull()
        this.district = updateAddressView.district.getValueOrNull()



    }


}
