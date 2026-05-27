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

package dev.pandasystems.pandalib.serializer

sealed interface JsonElement {
	val asNull: JsonNull? get() = this as? JsonNull
	val asObject: JsonObject? get() = this as? JsonObject
	val asArray: JsonArray? get() = this as? JsonArray
	val asPrimitive: JsonPrimitive? get() = this as? JsonPrimitive
}

object JsonNull : JsonElement {
	override val asNull: JsonNull get() = this
}

class JsonObject(
	content: Map<String, JsonElement> = emptyMap(),
) : JsonElement, MutableMap<String, JsonElement> by content.toMutableMap()

class JsonArray(
	content: List<JsonElement> = emptyList(),
) : JsonElement, MutableList<JsonElement> by content.toMutableList()

class JsonPrimitive private constructor(
	val content: String,
	val isString: Boolean
) : JsonElement {
	constructor(text: String) : this(text, true)
	constructor(boolean: Boolean) : this(boolean.toString(), false)
	constructor(number: Number) : this(number.toString(), false)
	
	override fun toString(): String = content
	
	val string: String get() = content
	val booleanOrNull: Boolean? get() = content.takeIf { !isString }?.toBooleanStrictOrNull()
	val boolean: Boolean get() = booleanOrNull ?: throw IllegalStateException("$this does not represent a Boolean")
	val intOrNull: Int? get() = content.takeIf { !isString }?.toIntOrNull()
	val int: Int get() = intOrNull ?: throw IllegalStateException("$this does not represent a Int")
	val floatOrNull: Float? get() = content.takeIf { !isString }?.toFloatOrNull()
	val float: Float get() = floatOrNull ?: throw IllegalStateException("$this does not represent a Float")
	val doubleOrNull: Double? get() = content.takeIf { !isString }?.toDoubleOrNull()
	val double: Double get() = doubleOrNull ?: throw IllegalStateException("$this does not represent a Double")
	val longOrNull: Long? get() = content.takeIf { !isString }?.toLongOrNull()
	val long: Long get() = longOrNull ?: throw IllegalStateException("$this does not represent a Long")
}