package pabulo.teste.front.dtos.sellers

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image

data class SaveSellerDto(

    val sellerRca: SimpleIntegerProperty,
    val sellerName: SimpleStringProperty,
    val sellerImagePath: SimpleStringProperty,

){

constructor(

    sellerRca: Int,
    sellerName: String,
    sellerImagePath: String

): this(

    SimpleIntegerProperty(sellerRca),
    SimpleStringProperty(sellerName),
    SimpleStringProperty(sellerImagePath)


)






}


data class SaveSellerDtoToDb(

    var sellerRca: Int,
    var sellerName: String,
    var sellerImagePath: String

){

    fun convertDTO(saveSellerDto: SaveSellerDto){

        this.sellerRca = saveSellerDto.sellerRca.get()
        this.sellerName = saveSellerDto.sellerName.get()
        this.sellerImagePath = saveSellerDto.sellerImagePath.get()



    }


}







