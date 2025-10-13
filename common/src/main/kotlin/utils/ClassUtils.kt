/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

@file:JvmName("ClassUtils")

package dev.pandasystems.pandalib.utils

import java.lang.reflect.Field

fun <T> Class<T>.constructClassUnsafely(): T {
	val constructor = getDeclaredConstructor()
	constructor.isAccessible = true
	return constructor.newInstance()
}

fun <T> Field.setFieldUnsafely(parentObj: Any, value: T) {
	set(parentObj, value)
}

fun <T> Field.getFieldUnsafely(parentObj: Any): T {
	@Suppress("UNCHECKED_CAST")
	return get(parentObj) as T
}