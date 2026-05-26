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

package dev.pandasystems.pandalib.serializer.typeserializers.types

import dev.pandasystems.pandalib.serializer.JsonElement
import dev.pandasystems.pandalib.serializer.JsonPrimitive
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer
import java.util.UUID

class UuidSerializer : TypeSerializer<UUID> {
	override fun serialize(value: UUID): JsonElement {
		return JsonPrimitive(value.toString())
	}

	override fun deserialize(json: JsonElement): UUID {
		require(json is JsonPrimitive) { "Expected JsonPrimitive for UUID deserialization, got ${json.javaClass.simpleName}" }
		require(json.isString) { "Expected string value for UUID deserialization, got ${json.content}" }
		return UUID.fromString(json.string)
	}
}