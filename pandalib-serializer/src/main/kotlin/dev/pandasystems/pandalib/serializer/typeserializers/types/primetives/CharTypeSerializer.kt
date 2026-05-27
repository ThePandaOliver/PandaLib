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
