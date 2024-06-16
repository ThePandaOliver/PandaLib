/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.core.utils.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.io.IOException;

public class Vector3fTypeAdapter extends TypeAdapter<Vector3fc> {
	@Override
	public void write(JsonWriter jsonWriter, Vector3fc vector3fc) throws IOException {
		jsonWriter.beginArray();
		jsonWriter.value(vector3fc.x());
		jsonWriter.value(vector3fc.y());
		jsonWriter.value(vector3fc.z());
		jsonWriter.endArray();
	}

	@Override
	public Vector3fc read(JsonReader jsonReader) throws IOException {
		jsonReader.beginArray();
		float x = (float) jsonReader.nextDouble();
		float y = (float) jsonReader.nextDouble();
		float z = (float) jsonReader.nextDouble();
		jsonReader.endArray();
		return new Vector3f(x, y, z);
	}
}