package dev.pandasystems.pandalib.core.utils

import java.lang.reflect.Field

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