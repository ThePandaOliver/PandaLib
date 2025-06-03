@file:JvmName("ClassUtils")

package dev.pandasystems.pandalib.utils

import java.lang.reflect.Field

fun <T : Any> constructClassUnsafely(clazz: Class<T>): T {
	val constructor = clazz.getDeclaredConstructor()
	constructor.isAccessible = true
	return constructor.newInstance()
}

fun <T : Any> setFieldUnsafely(parentObj: Any, field: Field, value: T?) {
	field.set(parentObj, value)
}

fun <T : Any> getFieldUnsafely(parentObj: Any, field: Field): T? {
	@Suppress("UNCHECKED_CAST")
	return field.get(parentObj) as T?
}