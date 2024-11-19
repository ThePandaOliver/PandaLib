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

package me.pandamods.pandalib.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;

public class NBTUtils {
	public static Tag convertJsonToTag(JsonElement jsonElement) {
		if (jsonElement.isJsonPrimitive()) {
			JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
			if (primitive.isString())
				return StringTag.valueOf(primitive.getAsString());
			else if (primitive.isNumber())
				return DoubleTag.valueOf(primitive.getAsDouble());
			else if (primitive.isBoolean())
				return ByteTag.valueOf(primitive.getAsBoolean());
		} else if (jsonElement.isJsonArray()) {
			JsonArray array = jsonElement.getAsJsonArray();
			ListTag list = new ListTag();
			array.forEach(element -> list.add(convertJsonToTag(element)));
			return list;
		} else if (jsonElement.isJsonObject()) {
			CompoundTag compound = new CompoundTag();
			JsonObject object = jsonElement.getAsJsonObject();
			object.entrySet().forEach(entry -> compound.put(entry.getKey(), convertJsonToTag(entry.getValue())));
			return compound;
		}
		return null;
	}

	public static JsonElement convertTagToJson(Tag tag) {
		if (tag instanceof StringTag)
			return new JsonPrimitive(tag.getAsString());
		else if (tag instanceof DoubleTag) {
			return new JsonPrimitive(((DoubleTag) tag).getAsDouble());
		} else if (tag instanceof ByteTag byteTag) {
			return new JsonPrimitive(byteTag == ByteTag.ONE);
		} else if (tag instanceof CompoundTag compoundTag) {
			JsonObject object = new JsonObject();
			compoundTag.getAllKeys().forEach(key ->
					object.add(key, convertTagToJson(compoundTag.get(key)))
			);
			return object;
		} else if (tag instanceof ListTag listTag) {
			JsonArray array = new JsonArray();
			listTag.forEach(element ->
					array.add(convertTagToJson(element))
			);
			return array;
		}
		return null;
	}
}
