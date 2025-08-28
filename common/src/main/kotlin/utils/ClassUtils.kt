/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

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