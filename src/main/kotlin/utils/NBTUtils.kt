package dev.pandasystems.pandalib.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.minecraft.nbt.*
import java.util.function.Consumer

object NBTUtils {
	@JvmStatic
	fun convertJsonToTag(jsonElement: JsonElement): Tag? {
		if (jsonElement.isJsonPrimitive) {
			val primitive = jsonElement.getAsJsonPrimitive()
			if (primitive.isString) return StringTag.valueOf(primitive.asString)
			else if (primitive.isNumber) return DoubleTag.valueOf(primitive.asDouble)
			else if (primitive.isBoolean) return ByteTag.valueOf(primitive.asBoolean)
		} else if (jsonElement.isJsonArray) {
			val array = jsonElement.getAsJsonArray()
			val list = ListTag()
			array.forEach(Consumer { element: JsonElement -> list.add(convertJsonToTag(element)) })
			return list
		} else if (jsonElement.isJsonObject) {
			val compound = CompoundTag()
			val `object` = jsonElement.getAsJsonObject()
			`object`.entrySet().forEach(Consumer { entry: MutableMap.MutableEntry<String, JsonElement> ->
				compound.put(
					entry.key,
					convertJsonToTag(entry.value)!!
				)
			})
			return compound
		}
		return null
	}

	@JvmStatic
	fun convertTagToJson(tag: Tag?): JsonElement? {
		when (tag) {
			is StringTag -> return JsonPrimitive(tag.asString)
			is DoubleTag -> {
				return JsonPrimitive(tag.asDouble)
			}

			is ByteTag -> {
				return JsonPrimitive(tag === ByteTag.ONE)
			}

			is CompoundTag -> {
				val `object` = JsonObject()
				tag.allKeys.forEach(Consumer { key: String -> `object`.add(key, convertTagToJson(tag.get(key)!!)) }
				)
				return `object`
			}

			is ListTag -> {
				val array = JsonArray()
				tag.forEach(Consumer { element: Tag -> array.add(convertTagToJson(element)) })
				return array
			}

			else -> return null
		}
	}
}
