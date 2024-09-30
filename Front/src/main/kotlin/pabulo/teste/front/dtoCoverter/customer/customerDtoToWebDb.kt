package pabulo.teste.front.dtoCoverter.customer

import pabulo.teste.front.entity.Customer

data class CustomerDtoToWebDb(

    val name: String,
    var customerCode: Long,
    val phone: String,
    val customerType: String,
    val customerRegistered: String
)


fun convertLocalCustomerDto(customerlocal: Customer): CustomerDtoToWebDb {

    return CustomerDtoToWebDb(
        name = customerlocal.customerName,
        customerCode = customerlocal.customerCode.toLong(),
        phone = customerlocal.phone,
        customerType = customerlocal.customerType,
        customerRegistered = customerlocal.customerRegistered
    )
}


