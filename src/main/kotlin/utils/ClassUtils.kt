/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

@file:JvmName("ClassUtils")

package dev.pandasystems.pandalib.utils

import java.lang.reflect.Field
import kotlin.reflect.KClass

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

val KClass<*>.objects: Map<KClass<*>, Any>
	get() = this.nestedClasses
		.mapNotNull { it.objectInstance }
		.associateBy { it::class }
