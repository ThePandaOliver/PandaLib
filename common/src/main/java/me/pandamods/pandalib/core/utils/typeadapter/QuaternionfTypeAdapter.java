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
import java.io.IOException;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

public class QuaternionfTypeAdapter extends TypeAdapter<Quaternionfc> {
    public void write(JsonWriter jsonWriter, Quaternionfc quaternion) throws IOException {
		writeQuaternion(jsonWriter, quaternion);
    }

	public static void writeQuaternion(JsonWriter jsonWriter, Quaternionfc quaternion) throws IOException {
		jsonWriter.beginArray();
		jsonWriter.value(quaternion.x());
		jsonWriter.value(quaternion.y());
		jsonWriter.value(quaternion.z());
		jsonWriter.value(quaternion.w());
		jsonWriter.endArray();
	}

    public Quaternionf read(JsonReader jsonReader) throws IOException {
        return readQuaternion(jsonReader);
    }

	public static Quaternionf readQuaternion(JsonReader jsonReader) throws IOException {
		jsonReader.beginArray();
		double x = jsonReader.nextDouble();
		double y = jsonReader.nextDouble();
		double z = jsonReader.nextDouble();
		double w = jsonReader.nextDouble();
		jsonReader.endArray();
		return new Quaternionf(x, y, z, w);
	}
}
