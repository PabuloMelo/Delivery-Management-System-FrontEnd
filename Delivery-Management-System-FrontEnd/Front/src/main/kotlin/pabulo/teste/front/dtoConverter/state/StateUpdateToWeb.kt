package pabulo.teste.front.dtoConverter.state

import pabulo.teste.front.dtos.state.StateUpdateDTOtoWebDB
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StateUpdateToWeb{


    data class StateUpdateWeb(

        val orderNumber: Long?,
        val state: String?,
        val stateNV1: String?,
        val stateNV2: String?,
        val description: String?,
        val solveDriver: String?,
        val solveDate: LocalDate?,

    )


    fun convertLocalStateDtoToWeb(stateDtoLocal: StateUpdateDTOtoWebDB): StateUpdateWeb{


        return StateUpdateWeb(

            orderNumber = stateDtoLocal.orderNumber,
            state = stateDtoLocal.state?.let { adapterStringInEnumDb(it)},
            stateNV1 = stateDtoLocal.stateNV1?.let { adapterStringInEnumDb(it) },
            stateNV2 = stateDtoLocal.stateNV2?.let { adapterStringInEnumDb(it) },
            description = stateDtoLocal.description,
            solveDriver = stateDtoLocal.solveDriver?.let { adapterStringInEnumDb(it) },
            solveDate = stateDtoLocal.solveDate?.let { convertStringInToLocalDate(it) },
        )

    }

    private fun adapterStringInEnumDb(string: String): String {

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


    private fun convertStringInToLocalDate(dateAtConverter: String): LocalDate {

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateAtConverter, dateFormatter)


        return date

    }




}