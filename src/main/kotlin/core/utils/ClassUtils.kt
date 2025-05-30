package dev.pandasystems.pandalib.core.utils

import com.google.gson.Gson
import java.lang.reflect.Field
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

fun <T> constructUnsafely(cls: Class<T>): T {
	try {
		val constructor = cls.getDeclaredConstructor()
		constructor.setAccessible(true)
		return constructor.newInstance()
	} catch (e: ReflectiveOperationException) {
		throw RuntimeException(e)
	}
}

fun <T> setFieldUnsafely(parentObject: Any, field: Field, value: T) {
	try {
		field.set(parentObject, value)
	} catch (e: IllegalAccessException) {
		throw RuntimeException(e)
	}
}

fun <T> getFieldUnsafely(parentObject: Any, field: Field): T {
	try {
		@Suppress("UNCHECKED_CAST")
		return field.get(parentObject) as T
	} catch (e: IllegalAccessException) {
		throw RuntimeException(e)
	}
}

fun doesClassExist(className: String): Boolean {
	try {
		Class.forName(className)
		return true
	} catch (e: ClassNotFoundException) {
		return false
	}
}

fun <T : Any> T.updateFromJson(json: String, gson: Gson = Gson()): T {
	val source = gson.fromJson(json, this::class.java)

	this::class.memberProperties.forEach { property ->
		if (property is KMutableProperty<*>) {
			property.isAccessible = true
			@Suppress("UNCHECKED_CAST")
			val mutableProperty = property as KMutableProperty1<T, Any?>
			val value = mutableProperty.get(source)
			mutableProperty.set(this, value)
		}
	}

	return this
}