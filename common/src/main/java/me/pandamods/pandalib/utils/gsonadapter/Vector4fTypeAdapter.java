package me.pandamods.pandalib.utils.gsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class Vector4fTypeAdapter extends TypeAdapter<Vector4f> {
	@Override
	public void write(JsonWriter out, Vector4f vector) throws IOException {
		out.beginArray();
		out.value(vector.x);
		out.value(vector.y);
		out.value(vector.z);
		out.value(vector.w);
		out.endArray();
	}

	@Override
	public Vector4f read(JsonReader in) throws IOException {
		in.beginArray();
		float x = (float) in.nextDouble();
		float y = (float) in.nextDouble();
		float z = (float) in.nextDouble();
		float w = (float) in.nextDouble();
		in.endArray();
		return new Vector4f(x, y, z, w);
	}
}
