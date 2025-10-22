/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.utils.codecs

import dev.pandasystems.universalserializer.elements.*
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

object TreeElementCodec : StreamCodec<FriendlyByteBuf, TreeElement> {
	// Type identifiers for efficient encoding
	private const val TYPE_NULL = 0.toByte()
	private const val TYPE_PRIMITIVE = 1.toByte()
	private const val TYPE_OBJECT = 2.toByte()
	private const val TYPE_ARRAY = 3.toByte()

	override fun decode(byteBuf: FriendlyByteBuf): TreeElement {
		return when (val type = byteBuf.readByte()) {
			TYPE_NULL -> TreeNull
			TYPE_PRIMITIVE -> TreePrimitiveCodec.decode(byteBuf)
			TYPE_OBJECT -> TreeObjectCodec.decode(byteBuf)
			TYPE_ARRAY -> TreeArrayCodec.decode(byteBuf)
			else -> throw IllegalArgumentException("Unknown element type: $type")
		}
	}

	override fun encode(byteBuf: FriendlyByteBuf, element: TreeElement) {
		when {
			element.isNull -> {
				byteBuf.writeByte(TYPE_NULL.toInt())
			}
			element.isPrimitive -> {
				byteBuf.writeByte(TYPE_PRIMITIVE.toInt())
				TreePrimitiveCodec.encode(byteBuf, element.asPrimitive)
			}
			element.isObject -> {
				byteBuf.writeByte(TYPE_OBJECT.toInt())
				TreeObjectCodec.encode(byteBuf, element.asObject)
			}
			element.isArray -> {
				byteBuf.writeByte(TYPE_ARRAY.toInt())
				TreeArrayCodec.encode(byteBuf, element.asArray)
			}
			else -> throw IllegalArgumentException("Unknown JSON element type: ${element.javaClass}")
		}
	}
}

object TreeObjectCodec : StreamCodec<FriendlyByteBuf, TreeObject> {
	override fun decode(byteBuf: FriendlyByteBuf): TreeObject {
		val size = byteBuf.readVarInt()
		val obj = TreeObject()
		repeat(size) {
			val key = byteBuf.readUtf()
			val value = TreeElementCodec.decode(byteBuf)
			obj[key] = value
		}
		return obj
	}

	override fun encode(byteBuf: FriendlyByteBuf, obj: TreeObject) {
		byteBuf.writeVarInt(obj.size)
		for ((key, value) in obj.entries) {
			byteBuf.writeUtf(key)
			TreeElementCodec.encode(byteBuf, value)
		}
	}
}

object TreeArrayCodec : StreamCodec<FriendlyByteBuf, TreeArray> {
	override fun decode(byteBuf: FriendlyByteBuf): TreeArray {
		val size = byteBuf.readVarInt()
		val array = TreeArray()
		repeat(size) {
			array.add(TreeElementCodec.decode(byteBuf))
		}
		return array
	}

	override fun encode(byteBuf: FriendlyByteBuf, array: TreeArray) {
		byteBuf.writeVarInt(array.size)
		for (element in array) {
			TreeElementCodec.encode(byteBuf, element)
		}
	}
}

object TreePrimitiveCodec : StreamCodec<FriendlyByteBuf, TreePrimitive> {
	// Type identifiers for primitives
	private const val TYPE_BOOLEAN = 0.toByte()
	private const val TYPE_NUMBER_BYTE = 1.toByte()
	private const val TYPE_NUMBER_SHORT = 2.toByte()
	private const val TYPE_NUMBER_INT = 3.toByte()
	private const val TYPE_NUMBER_LONG = 4.toByte()
	private const val TYPE_NUMBER_FLOAT = 5.toByte()
	private const val TYPE_NUMBER_DOUBLE = 6.toByte()
	private const val TYPE_STRING = 7.toByte()

	override fun decode(byteBuf: FriendlyByteBuf): TreePrimitive {
		return when (val type = byteBuf.readByte()) {
			TYPE_BOOLEAN -> TreePrimitive(byteBuf.readBoolean())
			TYPE_NUMBER_BYTE -> TreePrimitive(byteBuf.readByte())
			TYPE_NUMBER_SHORT -> TreePrimitive(byteBuf.readShort())
			TYPE_NUMBER_INT -> TreePrimitive(byteBuf.readVarInt())
			TYPE_NUMBER_LONG -> TreePrimitive(byteBuf.readVarLong())
			TYPE_NUMBER_FLOAT -> TreePrimitive(byteBuf.readFloat())
			TYPE_NUMBER_DOUBLE -> TreePrimitive(byteBuf.readDouble())
			TYPE_STRING -> TreePrimitive(byteBuf.readUtf())
			else -> throw IllegalArgumentException("Unknown JSON primitive type: $type")
		}
	}

	override fun encode(byteBuf: FriendlyByteBuf, primitive: TreePrimitive) {
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