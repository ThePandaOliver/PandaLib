/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.serializer.typeserializers.types.primetives

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
