/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.utils.codecs

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import dev.pandasystems.pandalib.utils.codec.StreamCodec
import net.minecraft.network.FriendlyByteBuf

object JsonElementCodec : StreamCodec<FriendlyByteBuf, JsonElement> {
	// Type identifiers for efficient encoding
	private const val TYPE_NULL = 0.toByte()
	private const val TYPE_PRIMITIVE = 1.toByte()
	private const val TYPE_OBJECT = 2.toByte()
	private const val TYPE_ARRAY = 3.toByte()

	override fun decode(byteBuf: FriendlyByteBuf): JsonElement {
		return when (val type = byteBuf.readByte()) {
			TYPE_NULL -> com.google.gson.JsonNull.INSTANCE
			TYPE_PRIMITIVE -> JsonPrimitiveCodec.decode(byteBuf)
			TYPE_OBJECT -> JsonObjectCodec.decode(byteBuf)
			TYPE_ARRAY -> JsonArrayCodec.decode(byteBuf)
			else -> throw IllegalArgumentException("Unknown JSON element type: $type")
		}
	}

	override fun encode(byteBuf: FriendlyByteBuf, element: JsonElement) {
		when {
			element.isJsonNull -> {
				byteBuf.writeByte(TYPE_NULL.toInt())
			}
			element.isJsonPrimitive -> {
				byteBuf.writeByte(TYPE_PRIMITIVE.toInt())
				JsonPrimitiveCodec.encode(byteBuf, element.asJsonPrimitive)
			}
			element.isJsonObject -> {
				byteBuf.writeByte(TYPE_OBJECT.toInt())
				JsonObjectCodec.encode(byteBuf, element.asJsonObject)
			}
			element.isJsonArray -> {
				byteBuf.writeByte(TYPE_ARRAY.toInt())
				JsonArrayCodec.encode(byteBuf, element.asJsonArray)
			}
			else -> throw IllegalArgumentException("Unknown JSON element type: ${element.javaClass}")
		}
	}
}

object JsonObjectCodec : StreamCodec<FriendlyByteBuf, JsonObject> {
	override fun decode(byteBuf: FriendlyByteBuf): JsonObject {
		val size = byteBuf.readVarInt()
		val obj = JsonObject()
		repeat(size) {
			val key = byteBuf.readUtf()
			val value = JsonElementCodec.decode(byteBuf)
			obj.add(key, value)
		}
		return obj
	}

	override fun encode(byteBuf: FriendlyByteBuf, obj: JsonObject) {
		byteBuf.writeVarInt(obj.size())
		for ((key, value) in obj.entrySet()) {
			byteBuf.writeUtf(key)
			JsonElementCodec.encode(byteBuf, value)
		}
	}
}

object JsonArrayCodec : StreamCodec<FriendlyByteBuf, JsonArray> {
	override fun decode(byteBuf: FriendlyByteBuf): JsonArray {
		val size = byteBuf.readVarInt()
		val array = JsonArray(size)
		repeat(size) {
			array.add(JsonElementCodec.decode(byteBuf))
		}
		return array
	}

	override fun encode(byteBuf: FriendlyByteBuf, array: JsonArray) {
		byteBuf.writeVarInt(array.size())
		for (element in array) {
			JsonElementCodec.encode(byteBuf, element)
		}
	}
}

object JsonPrimitiveCodec : StreamCodec<FriendlyByteBuf, JsonPrimitive> {
	// Type identifiers for primitives
	private const val TYPE_BOOLEAN = 0.toByte()
	private const val TYPE_NUMBER_BYTE = 1.toByte()
	private const val TYPE_NUMBER_SHORT = 2.toByte()
	private const val TYPE_NUMBER_INT = 3.toByte()
	private const val TYPE_NUMBER_LONG = 4.toByte()
	private const val TYPE_NUMBER_FLOAT = 5.toByte()
	private const val TYPE_NUMBER_DOUBLE = 6.toByte()
	private const val TYPE_STRING = 7.toByte()

	override fun decode(byteBuf: FriendlyByteBuf): JsonPrimitive {
		return when (val type = byteBuf.readByte()) {
			TYPE_BOOLEAN -> JsonPrimitive(byteBuf.readBoolean())
			TYPE_NUMBER_BYTE -> JsonPrimitive(byteBuf.readByte())
			TYPE_NUMBER_SHORT -> JsonPrimitive(byteBuf.readShort())
			TYPE_NUMBER_INT -> JsonPrimitive(byteBuf.readVarInt())
			TYPE_NUMBER_LONG -> JsonPrimitive(byteBuf.readVarLong())
			TYPE_NUMBER_FLOAT -> JsonPrimitive(byteBuf.readFloat())
			TYPE_NUMBER_DOUBLE -> JsonPrimitive(byteBuf.readDouble())
			TYPE_STRING -> JsonPrimitive(byteBuf.readUtf())
			else -> throw IllegalArgumentException("Unknown JSON primitive type: $type")
		}
	}

	override fun encode(byteBuf: FriendlyByteBuf, primitive: JsonPrimitive) {
		when {
			primitive.isBoolean -> {
				byteBuf.writeByte(TYPE_BOOLEAN.toInt())
				byteBuf.writeBoolean(primitive.asBoolean)
			}
			primitive.isNumber -> {
				// Optimize number encoding based on the actual type and value range
				when (val number = primitive.asNumber) {
					is Byte -> {
						byteBuf.writeByte(TYPE_NUMBER_BYTE.toInt())
						byteBuf.writeByte(number.toInt())
					}
					is Short -> {
						byteBuf.writeByte(TYPE_NUMBER_SHORT.toInt())
						byteBuf.writeShort(number.toInt())
					}
					is Int -> {
						byteBuf.writeByte(TYPE_NUMBER_INT.toInt())
						byteBuf.writeVarInt(number)
					}
					is Long -> {
						byteBuf.writeByte(TYPE_NUMBER_LONG.toInt())
						byteBuf.writeVarLong(number)
					}
					is Float -> {
						byteBuf.writeByte(TYPE_NUMBER_FLOAT.toInt())
						byteBuf.writeFloat(number)
					}
					is Double -> {
						byteBuf.writeByte(TYPE_NUMBER_DOUBLE.toInt())
						byteBuf.writeDouble(number)
					}
					else -> {
						// Fallback: try to determine best fit
						val longValue = number.toLong()
						if (number.toDouble() == longValue.toDouble()) {
							// It's an integral number
							when (longValue) {
								in Byte.MIN_VALUE..Byte.MAX_VALUE -> {
									byteBuf.writeByte(TYPE_NUMBER_BYTE.toInt())
									byteBuf.writeByte(longValue.toInt())
								}
								in Short.MIN_VALUE..Short.MAX_VALUE -> {
									byteBuf.writeByte(TYPE_NUMBER_SHORT.toInt())
									byteBuf.writeShort(longValue.toInt())
								}
								in Int.MIN_VALUE..Int.MAX_VALUE -> {
									byteBuf.writeByte(TYPE_NUMBER_INT.toInt())
									byteBuf.writeVarInt(longValue.toInt())
								}
								else -> {
									byteBuf.writeByte(TYPE_NUMBER_LONG.toInt())
									byteBuf.writeVarLong(longValue)
								}
							}
						} else {
							// It's a floating point number
							val floatValue = number.toFloat()
							if (floatValue.toDouble() == number.toDouble()) {
								byteBuf.writeByte(TYPE_NUMBER_FLOAT.toInt())
								byteBuf.writeFloat(floatValue)
							} else {
								byteBuf.writeByte(TYPE_NUMBER_DOUBLE.toInt())
								byteBuf.writeDouble(number.toDouble())
							}
						}
					}
				}
			}
			primitive.isString -> {
				byteBuf.writeByte(TYPE_STRING.toInt())
				byteBuf.writeUtf(primitive.asString)
			}
			else -> throw IllegalArgumentException("Unknown JSON primitive type")
		}
	}
}