package dev.pandasystems.pandalib.serializer.typeserializers.types

import dev.pandasystems.pandalib.serializer.JsonElement
import dev.pandasystems.pandalib.serializer.JsonPrimitive
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer

class LongSerializer : TypeSerializer<Long> {
    override fun serialize(value: Long): JsonElement {
        return JsonPrimitive(value)
    }

    override fun deserialize(json: JsonElement): Long {
        if (json !is JsonPrimitive) {
            throw IllegalArgumentException("Expected JsonPrimitive for Long, got ${json::class.simpleName}")
        }

        return json.longOrNull
            ?: throw IllegalArgumentException("Expected Long primitive, got $json")
    }
}
