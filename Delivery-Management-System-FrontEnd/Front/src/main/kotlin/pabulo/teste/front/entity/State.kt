package pabulo.teste.front.entity

data class State(

    val orderCodeState: Int,
    val customerCode: Int,
    val loadCode: Int,
    val customerName: String,
    val state: String,
    val stateNV1: String,
    val stateNV2: String,
    val description: String,
    val invoiceDate: String,
    val purchaseDate: String,
    val solveDriver: String,
    val solveDate: String,
    val resolve: String,
    var stateThrowble: String = " "


)
