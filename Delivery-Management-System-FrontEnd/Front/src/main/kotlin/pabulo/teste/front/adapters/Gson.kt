package pabulo.teste.front.adapters

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.LocalDate

object GsonProvider {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java,LocalDataAdapter())
        .create()
}