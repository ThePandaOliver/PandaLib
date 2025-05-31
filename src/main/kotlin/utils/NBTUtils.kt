@file:JvmName("NBTUtils")

package dev.pandasystems.pandalib.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.*
import net.minecraft.nbt.*
import java.util.function.Consumer

fun convertJsonToTag(node: JsonNode): Tag? {
	return when {
		node.isValueNode -> when {
			node.isTextual -> StringTag.valueOf(node.asText())
			node.isBinary -> StringTag.valueOf(node.binaryValue().decodeToString())
			node.isDouble -> DoubleTag.valueOf(node.asDouble())
			node.isFloat -> FloatTag.valueOf(node.asDouble().toFloat())
			node.isInt -> IntTag.valueOf(node.asInt())
			node.isLong -> LongTag.valueOf(node.asLong())
			node.isShort -> ShortTag.valueOf(node.asInt().toShort())
			node.isBigDecimal -> LongTag.valueOf(node.asLong())
			node.isBigInteger -> IntTag.valueOf(node.asInt())
			node.isBoolean -> ByteTag.valueOf(node.asBoolean())
			else -> null
		}

		node.isArray -> {
			val list = ListTag()
			node.forEach { child -> convertJsonToTag(child)?.let { list.add(it) } }
			list
		}

		node.isObject -> {
			val compound = CompoundTag()
			node.fields().forEach { (key, value) -> convertJsonToTag(value)?.let { compound.put(key, it) } }
			compound
		}

		else -> null
	}
}

@JvmOverloads
fun convertTagToJson(tag: Tag?, mapper: ObjectMapper = ObjectMapper()): JsonNode? {
	return when (tag) {
		is StringTag -> TextNode(tag.asString)
		is DoubleTag -> return DoubleNode(tag.asDouble)
		is FloatTag -> return FloatNode(tag.asFloat)
		is IntTag -> return IntNode(tag.asInt)
		is LongTag -> return LongNode(tag.asLong)
		is ShortTag -> return IntNode(tag.asShort.toInt())
		is ByteTag -> return BooleanNode.valueOf(tag.asByte != 0.toByte())
		is CompoundTag -> mapper.createObjectNode()
			.also { tag.allKeys.forEach { key -> it.putIfAbsent(key, convertTagToJson(tag.get(key))) } }

		is ListTag -> mapper.createArrayNode()
			.also { tag.forEach(Consumer { element: Tag -> it.add(convertTagToJson(element)) }) }

		else -> return null
	}
}
