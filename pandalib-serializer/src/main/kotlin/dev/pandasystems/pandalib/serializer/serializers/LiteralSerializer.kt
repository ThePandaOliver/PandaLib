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

interface LiteralSerializer : Serializer<String> {
	fun decodeFile(file: File): JsonElement = decode(file.readText())
	fun <Obj : Any> decodeFileToObject(file: File, dist: Obj): Obj = decodeToObject(file.readText(), dist)
	fun encodeToFile(json: JsonElement, file: File) = file.writeText(encode(json))
	fun <Obj : Any> encodeObjectToFile(obj: Obj, file: File) = file.writeText(encodeObject(obj))
}