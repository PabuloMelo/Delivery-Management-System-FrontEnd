package pabulo.teste.front.dtoConverter.load

import pabulo.teste.front.entity.Load
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class LoadDtoToWebDb(

    val loadNumber: Long?,
    val driver: String,
    val depurateDate: LocalDate,

    )
fun converterLoadLocalToWeb(loadLocal: Load): LoadDtoToWebDb{

    return LoadDtoToWebDb(

        loadNumber = loadLocal.loadCode.toLong(),
        driver = adapterStringInEnumnDb(loadLocal.driver),
        depurateDate = convertStringInToLocalDate(loadLocal.departureDate)
    )

}

fun convertStringInToLocalDate(dateAtConverter: String):LocalDate {

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateAtConverter,dateFormatter)

    return date
}

fun adapterStringInEnumnDb(string: String): String{

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
        .replace(" ","_")


}
