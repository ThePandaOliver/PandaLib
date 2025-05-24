/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.pandamods.pandalib.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.minecraft.nbt.*
import java.util.function.Consumer

object NBTUtils {
	fun convertJsonToTag(jsonElement: JsonElement): Tag? {
		if (jsonElement.isJsonPrimitive()) {
			val primitive = jsonElement.getAsJsonPrimitive()
			if (primitive.isString()) return StringTag.valueOf(primitive.getAsString())
			else if (primitive.isNumber()) return DoubleTag.valueOf(primitive.getAsDouble())
			else if (primitive.isBoolean()) return ByteTag.valueOf(primitive.getAsBoolean())
		} else if (jsonElement.isJsonArray()) {
			val array = jsonElement.getAsJsonArray()
			val list = ListTag()
			array.forEach(Consumer { element: JsonElement -> list.add(convertJsonToTag(element!!)) })
			return list
		} else if (jsonElement.isJsonObject()) {
			val compound = CompoundTag()
			val `object` = jsonElement.getAsJsonObject()
			`object`.entrySet().forEach(Consumer { entry: MutableMap.MutableEntry<String, JsonElement> ->
				compound.put(
					entry!!.key,
					NBTUtils.convertJsonToTag(entry.value!!)
				)
			})
			return compound
		}
		return null
	}

	fun convertTagToJson(tag: Tag?): JsonElement? {
		if (tag is StringTag) return JsonPrimitive(tag.getAsString())
		else if (tag is DoubleTag) {
			return JsonPrimitive(tag.getAsDouble())
		} else if (tag is ByteTag) {
			return JsonPrimitive(tag === ByteTag.ONE)
		} else if (tag is CompoundTag) {
			val `object` = JsonObject()
			tag.getAllKeys().forEach(Consumer { key: String -> `object`.add(key, convertTagToJson(tag.get(key)!!)) }
			)
			return `object`
		} else if (tag is ListTag) {
			val array = JsonArray()
			tag.forEach(Consumer { element: Tag -> array.add(convertTagToJson(element)) }
			)
			return array
		}
		return null
	}
}
