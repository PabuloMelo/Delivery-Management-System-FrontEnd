package pabulo.teste.front.entity

data class Load (

    var loadCode: Int,
    val driver: String,
    val departureDate: String,
    val loadValidate: String,
    var loadThrowable: String = " ",
)
