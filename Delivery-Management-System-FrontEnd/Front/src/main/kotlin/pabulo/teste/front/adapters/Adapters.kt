package pabulo.teste.front.adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDataAdapter : TypeAdapter<LocalDate>() {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE


    override fun write(out: JsonWriter, value: LocalDate?) {

        if (value == null) {

            out.nullValue()

            return

        }

        out.value(value.format(formatter))

    }

    override fun read(reader: JsonReader): LocalDate? {
        return when (reader.peek()) {

            com.google.gson.stream.JsonToken.NULL -> {

                reader.nextNull()

                null

            }

            else -> LocalDate.parse(reader.nextString(), formatter)

        }
    }


}