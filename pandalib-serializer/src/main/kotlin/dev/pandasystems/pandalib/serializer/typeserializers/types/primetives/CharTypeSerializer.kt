package dev.pandasystems.pandalib.serializer.typeserializers.types

import dev.pandasystems.pandalib.serializer.JsonElement
import dev.pandasystems.pandalib.serializer.JsonPrimitive
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer

class CharSerializer : TypeSerializer<Char> {
    override fun serialize(value: Char): JsonElement {
        return JsonPrimitive(value.toString())
    }

    override fun deserialize(json: JsonElement): Char {
        if (json !is JsonPrimitive) {
            throw IllegalArgumentException("Expected JsonPrimitive for Char, got ${json::class.simpleName}")
        }

        val value = json.content

        if (value.length != 1) {
            throw IllegalArgumentException("Expected single-character String for Char, got $value")
        }

        return value[0]
    }
}
