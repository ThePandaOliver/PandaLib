package dev.pandasystems.pandalib.serializer.typeserializers.types

import dev.pandasystems.pandalib.serializer.JsonElement
import dev.pandasystems.pandalib.serializer.JsonPrimitive
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer

class StringSerializer : TypeSerializer<String> {
    override fun serialize(value: String): JsonElement {
        return JsonPrimitive(value)
    }

    override fun deserialize(json: JsonElement): String {
        if (json !is JsonPrimitive) {
            throw IllegalArgumentException("Expected JsonPrimitive for String, got ${json::class.simpleName}")
        }

        return json.content
    }
}
