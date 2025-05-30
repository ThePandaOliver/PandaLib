package dev.pandasystems.pandalib.utils.typeadapters

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class KotlinObjectTypeAdapter<T : Any>(private val objectInstance: T) : TypeAdapter<T>() {
	override fun write(writer: JsonWriter, value: T?) {
		if (value == null) {
			writer.nullValue()
		} else {
			writer.beginObject()
			writer.name("type").value(value::class.java.simpleName)
			writer.endObject()
		}
	}

	override fun read(reader: JsonReader): T {
		reader.beginObject()
		while (reader.hasNext()) {
			reader.nextName()
			reader.nextString()
		}
		reader.endObject()
		return objectInstance

	}
}

class KotlinObjectTypeAdapterFactory : TypeAdapterFactory {
	override fun <T : Any> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
		val rawType = type.rawType

		// Check if it's a Kotlin object
		if (rawType.kotlin.objectInstance != null) {
			@Suppress("UNCHECKED_CAST")
			val objectInstance = rawType.kotlin.objectInstance as T
			return KotlinObjectTypeAdapter(objectInstance)
		}

		return null
	}
}

fun GsonBuilder.registerKotlinTypeAdapter(): GsonBuilder = registerTypeAdapterFactory(KotlinObjectTypeAdapterFactory())
fun defaultKotlinGson(): Gson = GsonBuilder().registerKotlinTypeAdapter().create()