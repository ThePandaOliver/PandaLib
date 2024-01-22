package me.pandamods.pandalib.utils.gsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;

public class Vector2fTypeAdapter extends TypeAdapter<Vector2f> {
	@Override
	public void write(JsonWriter out, Vector2f vector) throws IOException {
		out.beginArray();
		out.value(vector.x);
		out.value(vector.y);
		out.endArray();
	}

	@Override
	public Vector2f read(JsonReader in) throws IOException {
		in.beginArray();
		float x = (float) in.nextDouble();
		float y = (float) in.nextDouble();
		in.endArray();
		return new Vector2f(x, y);
	}
}
