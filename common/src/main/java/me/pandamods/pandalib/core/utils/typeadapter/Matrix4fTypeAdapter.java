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
import org.joml.*;

import java.io.IOException;

public class Matrix4fTypeAdapter extends TypeAdapter<Matrix4fc> {
	@Override
	public void write(JsonWriter jsonWriter, Matrix4fc matrix4fc) throws IOException {
		jsonWriter.beginObject();
		jsonWriter.name("position");
		writeVector(jsonWriter, matrix4fc.getTranslation(new Vector3f()));

		jsonWriter.name("rotation");
		QuaternionfTypeAdapter.writeQuaternion(jsonWriter, matrix4fc.getUnnormalizedRotation(new Quaternionf()));

		jsonWriter.name("scale");
		writeVector(jsonWriter, matrix4fc.getScale(new Vector3f()));
		jsonWriter.endObject();
	}

	private void writeVector(JsonWriter jsonWriter, Vector3fc vector) throws IOException {
		jsonWriter.beginArray();
		jsonWriter.value(vector.x());
		jsonWriter.value(vector.y());
		jsonWriter.value(vector.z());
		jsonWriter.endArray();
	}

	@Override
	public Matrix4fc read(JsonReader jsonReader) throws IOException {
		Vector3f translation = null;
		Quaternionf rotation = null;
		Vector3f scale = null;

		jsonReader.beginObject();
		while (jsonReader.hasNext()) {
			String name = jsonReader.nextName();
			switch (name) {
				case "position":
					translation = readVector(jsonReader);
					break;
				case "rotation":
					rotation = new Quaternionf(QuaternionfTypeAdapter.readQuaternion(jsonReader));
					break;
				case "scale":
					scale = readVector(jsonReader);
					break;
				default:
					jsonReader.skipValue();
					break;
			}
		}
		jsonReader.endObject();

		return new Matrix4f().identity().translationRotateScale(translation, rotation, scale);
	}

	private Vector3f readVector(JsonReader jsonReader) throws IOException {
		jsonReader.beginArray();
		float x = (float) jsonReader.nextDouble();
		float y = (float) jsonReader.nextDouble();
		float z = (float) jsonReader.nextDouble();
		jsonReader.endArray();
		return new Vector3f(x, y, z);
	}
}
