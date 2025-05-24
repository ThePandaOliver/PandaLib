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
package me.pandamods.pandalib.utils

import java.lang.reflect.Field

@Suppress("unused")
object ClassUtils {
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
}
