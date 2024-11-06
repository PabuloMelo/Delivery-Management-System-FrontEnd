package pabulo.teste.front.dtos.customer

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

data class SaveCustomerDto(

    var customerCode: SimpleIntegerProperty,
    var customerName: SimpleStringProperty,
    var phone: SimpleStringProperty,
    var customerRegistered:  SimpleStringProperty,
    var customerType: SimpleStringProperty
) {
    constructor(

        customerCode: Int,
        customerName: String,
        phone: String,
        customerType: String,
        customerRegistered: String

    ) : this(

        SimpleIntegerProperty(customerCode),
        SimpleStringProperty(customerName),
        SimpleStringProperty(phone),
        SimpleStringProperty(customerType),
        SimpleStringProperty(customerRegistered)

    )














}