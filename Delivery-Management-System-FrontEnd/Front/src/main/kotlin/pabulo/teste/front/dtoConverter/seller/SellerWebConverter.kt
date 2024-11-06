package pabulo.teste.front.dtoConverter.seller

import pabulo.teste.front.entity.Seller

data class SellerWebConverter(

    val sellersName: String,
    val sellerRca: Long?,

    )

fun covertLocalSellerToWebDb(localSeller: Seller): SellerWebConverter {

    return SellerWebConverter(

        sellerRca = localSeller.sellerRca.toLong(),
        sellersName = adapterStringToWebDb(localSeller.sellerName)

    )

}

fun adapterStringToWebDb(string: String): String {

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