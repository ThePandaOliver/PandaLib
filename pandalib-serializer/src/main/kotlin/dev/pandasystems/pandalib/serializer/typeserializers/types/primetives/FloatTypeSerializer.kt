package dev.pandasystems.pandalib.serializer.typeserializers.types

import dev.pandasystems.pandalib.serializer.JsonElement
import dev.pandasystems.pandalib.serializer.JsonPrimitive
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer

class FloatSerializer : TypeSerializer<Float> {
    override fun serialize(value: Float): JsonElement {
        return JsonPrimitive(value)
    }

    override fun deserialize(json: JsonElement): Float {
        if (json !is JsonPrimitive) {
            throw IllegalArgumentException("Expected JsonPrimitive for Float, got ${json::class.simpleName}")
        }

        return json.floatOrNull
            ?: throw IllegalArgumentException("Expected Float primitive, got $json")
    }
}
