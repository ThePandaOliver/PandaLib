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

package dev.pandasystems.pandalib.serializer.serializers

import dev.pandasystems.pandalib.serializer.JsonElement
import java.io.File

interface Serializer<Value : Any> {
	// Codec
	fun decode(value: Value): JsonElement
	fun encode(json: JsonElement): Value
	
	// Serializer
	fun <Obj : Any> jsonToObject(json: JsonElement, dist: Obj): Obj {
		TODO("Not yet implemented")
	}
	fun <Obj : Any> objectToJson(obj: Obj): JsonElement {
		TODO("Not yet implemented")
	}
	
	// Extras
	fun <Obj : Any> decodeToObject(value: Value, dist: Obj): Obj = jsonToObject(decode(value), dist)
	fun <Obj : Any> encodeObject(obj: Obj): Value = encode(objectToJson(obj))
}