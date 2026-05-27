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

class EnumSerializer<E : Enum<E>>(
    private val enumType: Class<E>
) : TypeSerializer<E> {
    override fun serialize(value: E): JsonElement {
        return JsonPrimitive(value.name)
    }

    override fun deserialize(json: JsonElement): E {
        if (json !is JsonPrimitive) {
            throw IllegalArgumentException("Expected JsonPrimitive for enum ${enumType.name}, got ${json::class.simpleName}")
        }

        return java.lang.Enum.valueOf(enumType, json.content)
    }
}
