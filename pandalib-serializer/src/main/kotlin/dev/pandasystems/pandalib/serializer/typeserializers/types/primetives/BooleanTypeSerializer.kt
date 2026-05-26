package dev.pandasystems.pandalib.serializer.typeserializers.types

import dev.pandasystems.pandalib.serializer.JsonElement
import dev.pandasystems.pandalib.serializer.JsonPrimitive
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer

class BooleanSerializer : TypeSerializer<Boolean> {
    override fun serialize(value: Boolean): JsonElement {
        return JsonPrimitive(value)
    }

    override fun deserialize(json: JsonElement): Boolean {
        if (json !is JsonPrimitive) {
            throw IllegalArgumentException("Expected JsonPrimitive for Boolean, got ${json::class.simpleName}")
        }

        return json.booleanOrNull
            ?: throw IllegalArgumentException("Expected Boolean primitive, got $json")
    }
}
