package pabulo.teste.front.dtoConverter.customer

import pabulo.teste.front.dtos.customer.UpdateCustomerDtoToDb

data class CustomerUpdateToWeb(

    val name: String?,
    val phone: String?,
    val customerCode: Long?,
    val customerType: String?,
    val customerRegister: String?
)


fun convertLocalUpdateCustomer(updateCustomer: UpdateCustomerDtoToDb):CustomerUpdateToWeb{

    return CustomerUpdateToWeb(

        name = updateCustomer.customerName,
        phone = updateCustomer.phone,
        customerCode = updateCustomer.customerCode?.toLong(),
        customerType = updateCustomer.customerType,
        customerRegister = updateCustomer.customerRegistered
    )
}