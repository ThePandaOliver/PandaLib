package dev.pandasystems.pandalib.serializer.typeserializers.types

import dev.pandasystems.pandalib.serializer.JsonElement
import dev.pandasystems.pandalib.serializer.JsonPrimitive
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer

class DoubleSerializer : TypeSerializer<Double> {
    override fun serialize(value: Double): JsonElement {
        return JsonPrimitive(value)
    }

    override fun deserialize(json: JsonElement): Double {
        if (json !is JsonPrimitive) {
            throw IllegalArgumentException("Expected JsonPrimitive for Double, got ${json::class.simpleName}")
        }

        return json.doubleOrNull
            ?: throw IllegalArgumentException("Expected Double primitive, got $json")
    }
}
