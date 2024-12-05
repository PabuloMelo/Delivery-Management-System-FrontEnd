package pabulo.teste.front.dtoConverter.state

import pabulo.teste.front.entity.State
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class StateWebConverter(

    val orderNumber: Long,
    val state: String,
    val firstLevel: String,
    val secondLevel: String,
    val description: String,
    val solveDriver: String,
    val solveDate: LocalDate?,

)

fun convertLocalStateToWeb(localState: State): StateWebConverter {

    val state = StateWebConverter(

        orderNumber = localState.orderCodeState.toLong(),
        state = adapterStringInEnumnDb(localState.state),
        firstLevel = adapterStringInEnumnDb(localState.stateNV1),
        secondLevel = adapterStringInEnumnDb(localState.stateNV2),
        description = adapterStringInDescription(localState.description),
        solveDriver = adapterStringInEnumnDb(localState.solveDriver),
        solveDate = convertStringInToLocalDate(localState.solveDate),

    )

    println("Data null convertida ${state.solveDate} ")

    return state

}

data class StateWebToLocal(

    val orderNumber: Long,
    val customerName: String,
    val customerCode: Long,
    val loadCode: Long,
    val stateInit: String,
    val firstLevel: String,
    val secondLevel: String,
    val description: String,
    var solveDate: LocalDate?,
    val invoiceDate: LocalDate?,
    val purchaseDate: LocalDate,
    val solveDriver: String,
    var daysUntilSolve: Int?,
    val resolve: String
)

fun convertStateWebToLocal(stateWebToLocal: StateWebToLocal): State{

   return State(

        orderCodeState = stateWebToLocal.orderNumber.toInt(),
        customerCode = stateWebToLocal.customerCode.toInt(),
        loadCode = stateWebToLocal.loadCode.toInt(),
        customerName = stateWebToLocal.customerName,
        state = adapterStringWebToLocal( stateWebToLocal.stateInit),
        stateNV1 = adapterStringWebToLocal( stateWebToLocal.firstLevel),
        stateNV2 = adapterStringWebToLocal( stateWebToLocal.secondLevel),
        solveDate = adapterStringWebToLocal( stateWebToLocal.solveDriver),
        invoiceDate = stateWebToLocal.invoiceDate.toString().replace("-", "/"),
        purchaseDate = stateWebToLocal.purchaseDate.toString().replace("-", "/"),
        resolve = adapterStringWebToLocal( stateWebToLocal.resolve),
        description = stateWebToLocal.description,
        solveDriver = adapterStringWebToLocal( stateWebToLocal.solveDriver),

    )

}

fun convertStringInToLocalDate(dateAtConverter: String?): LocalDate? {

    if (dateAtConverter.isNullOrBlank()) {



        return null
    }

    return try {

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        LocalDate.parse(dateAtConverter, dateFormatter)

    } catch (e: Exception) {

        println("Erro ${e.message}")

        null

    }
}

fun adapterStringInEnumnDb(string: String): String {

    return string.trim().uppercase()
        .replace("Ç", "C")
        .replace("Ã", "A")
        .replace("Õ", "O")
        .replace("Á", "A")
        .replace("À", "A")
        .replace("É", "E")
        .replace("Í", "I")
        .replace("Ó", "O")
        .replace("Ú", "U")
        .replace("Ê", "E")
        .replace("Ô", "O")
        .replace("Â", "A")
        .replace(" ", "_")
}


fun adapterStringInDescription(string: String): String {

    return string.trim()
        .replace("Ç", "C")
        .replace("Ã", "A")
        .replace("Õ", "O")
        .replace("Á", "A")
        .replace("À", "A")
        .replace("É", "E")
        .replace("Í", "I")
        .replace("Ó", "O")
        .replace("Ú", "U")
        .replace("Ê", "E")
        .replace("Ô", "O")
        .replace("Â", "A")

}

fun adapterStringWebToLocal(string: String): String {

    return string.replace("_", " ")

}




