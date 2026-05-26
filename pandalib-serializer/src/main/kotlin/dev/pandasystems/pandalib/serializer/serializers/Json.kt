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

import dev.pandasystems.pandalib.serializer.JsonArray
import dev.pandasystems.pandalib.serializer.JsonElement
import dev.pandasystems.pandalib.serializer.JsonNull
import dev.pandasystems.pandalib.serializer.JsonObject
import dev.pandasystems.pandalib.serializer.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.Json as KJson
import kotlinx.serialization.json.JsonArray as KJsonArray
import kotlinx.serialization.json.JsonElement as KJsonElement
import kotlinx.serialization.json.JsonNull as KJsonNull
import kotlinx.serialization.json.JsonObject as KJsonObject
import kotlinx.serialization.json.JsonPrimitive as KJsonPrimitive

class Json(
	val prettyPrint: Boolean = true,
) : LiteralSerializer() {
	val json = KJson {
		encodeDefaults = true
		ignoreUnknownKeys = true
		prettyPrint = this@Json.prettyPrint
	}
	
	override fun decode(value: String): JsonElement {
		return json.parseToJsonElement(value).toPandaElement()
	}

	override fun encode(json: JsonElement): String {
		return this@Json.json.encodeToString(json.toKotlinJsonElement())
	}
	
	private fun KJsonElement.toPandaElement(): JsonElement {
		return when (this) {
			is KJsonNull -> JsonNull
			is KJsonObject -> JsonObject(mapValues { it.value.toPandaElement() })
			is KJsonArray -> JsonArray(map { it.toPandaElement() })
			is KJsonPrimitive -> when {
				isString -> JsonPrimitive(content)
				booleanOrNull != null -> JsonPrimitive(boolean)
				intOrNull != null -> JsonPrimitive(int)
				floatOrNull != null -> JsonPrimitive(float)
				doubleOrNull != null -> JsonPrimitive(double)
				longOrNull != null -> JsonPrimitive(long)
				else -> throw IllegalStateException("Unknown JSON primitive: $this")
			}
		}
	}
	
	private fun JsonElement.toKotlinJsonElement(): KJsonElement {
		return when (this) {
			is JsonObject -> KJsonObject(mapValues { it.value.toKotlinJsonElement() })
			is JsonArray -> KJsonArray(map { it.toKotlinJsonElement() })
			is JsonPrimitive -> when {
				isString != null -> KJsonPrimitive(content)
				booleanOrNull != null -> KJsonPrimitive(booleanOrNull)
				intOrNull != null -> KJsonPrimitive(intOrNull)
				floatOrNull != null -> KJsonPrimitive(floatOrNull)
				doubleOrNull != null -> KJsonPrimitive(doubleOrNull)
				longOrNull != null -> KJsonPrimitive(longOrNull)
				else -> throw IllegalStateException("Unknown JSON primitive: $this")
			}
			is JsonNull -> KJsonNull
		}
	}
}