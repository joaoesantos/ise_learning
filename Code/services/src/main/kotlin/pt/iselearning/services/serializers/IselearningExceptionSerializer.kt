package pt.iselearning.services.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import pt.iselearning.services.exception.IselearningException

class IselearningExceptionSerializer : JsonSerializer<IselearningException>() {
    override fun serialize(value: IselearningException?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen!!.writeStartObject()
        gen.writeStringField("ErrorMessage", value!!.outMessage)
        gen.writeStringField("ServerMessage", value.message)
        gen.writeEndObject()
    }
}
