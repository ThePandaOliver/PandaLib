package dev.pandasystems.pandalib.serializer.typeserializers.types

import dev.pandasystems.pandalib.serializer.JsonElement
import dev.pandasystems.pandalib.serializer.JsonPrimitive
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer

class IntSerializer : TypeSerializer<Int> {
    override fun serialize(value: Int): JsonElement {
        return JsonPrimitive(value)
    }

    override fun deserialize(json: JsonElement): Int {
        if (json !is JsonPrimitive) {
            throw IllegalArgumentException("Expected JsonPrimitive for Int, got ${json::class.simpleName}")
        }

        return json.intOrNull
            ?: throw IllegalArgumentException("Expected Int primitive, got $json")
    }
}
