/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.serializer.adapters

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

class LongTypeAdapter() : ConfigSerializerTypeAdapter<Long> {
	override fun serialize(src: Long): JsonElement {
		return JsonPrimitive(src)
	}

	override fun deserialize(element: JsonElement): Long {
		return element.asLong
	}
}