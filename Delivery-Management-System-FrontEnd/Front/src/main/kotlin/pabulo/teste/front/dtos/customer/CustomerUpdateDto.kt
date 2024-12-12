package pabulo.teste.front.dtos.customer

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty


fun SimpleIntegerProperty?.getValueOrNull(): Int? {

    return this?.value

}

fun SimpleStringProperty?.getValueOrNull(): String? {

    return this?.get()?.takeIf { it.isNotBlank() }

}

data class CustomerUpdateDto(
    val customerCode: SimpleIntegerProperty?,
    val customerName: SimpleStringProperty?,
    val phone: SimpleStringProperty?,
    val customerRegistered: SimpleStringProperty?,
    val customerType: SimpleStringProperty?
)

data class UpdateCustomerDtoToDb(
    var customerCode: Int? = null,
    var customerName: String? = null,
    var phone: String? = null,
    var customerRegistered: String? = null,
    var customerType: String? = null,


    ) {

    fun convertDto(updateCustomerDto: CustomerUpdateDto) {

        this.customerCode = updateCustomerDto.customerCode?.getValueOrNull()
        this.customerName = updateCustomerDto.customerName?.getValueOrNull()
        this.phone = updateCustomerDto.phone?.getValueOrNull()
        this.customerType = updateCustomerDto.customerType?.getValueOrNull()
        this.customerRegistered = updateCustomerDto.customerRegistered?.getValueOrNull()


    }


}


