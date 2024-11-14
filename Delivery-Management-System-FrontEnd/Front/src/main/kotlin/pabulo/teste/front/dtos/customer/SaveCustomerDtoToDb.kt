package pabulo.teste.front.dtos.customer

data class SaveCustomerDtoToDb(

    var customerCode: Int,
    var customerName: String,
    var phone: String,
    var customerRegistered: String,
    var customerType: String,
    var customerSync: String

) {


    fun covertDtos(saveCustomerDto: SaveCustomerDto) {

        this.customerCode = saveCustomerDto.customerCode.get()
        this.customerName = saveCustomerDto.customerName.get()
        this.phone = saveCustomerDto.phone.get()
        this.customerRegistered = saveCustomerDto.customerRegistered.get()
        this.customerType = saveCustomerDto.customerType.get()
        this.customerSync = "Não Sincronizado"


    }


}
