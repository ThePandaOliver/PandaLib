
package dev.pandasystems.pandalib.utils

import java.lang.reflect.Field

@Suppress("unused")
object ClassUtils {
	@JvmStatic
	fun <T> constructUnsafely(cls: Class<T>): T {
		try {
			val constructor = cls.getDeclaredConstructor()
			constructor.setAccessible(true)
			return constructor.newInstance()
		} catch (e: ReflectiveOperationException) {
			throw RuntimeException(e)
		}
	}

	@JvmStatic
	fun <T> setFieldUnsafely(parentObject: Any, field: Field, value: T) {
		try {
			field.set(parentObject, value)
		} catch (e: IllegalAccessException) {
			throw RuntimeException(e)
		}
	}

	@JvmStatic
	fun <T> getFieldUnsafely(parentObject: Any, field: Field): T {
		try {
			return field.get(parentObject) as T
		} catch (e: IllegalAccessException) {
			throw RuntimeException(e)
		}
	}

	@JvmStatic
	fun doesClassExist(className: String): Boolean {
		try {
			Class.forName(className)
			return true
		} catch (e: ClassNotFoundException) {
			return false
		}
	}
}
