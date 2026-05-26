/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.serializer.providers

import dev.pandasystems.pandalib.serializer.TypeSerializerContext
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializerFactory

internal fun TypeSerializerProvider.findDirect(type: Class<*>): (() -> TypeSerializer<*>)? {
	return typeSerializers[normalizeType(type)]
}

internal fun TypeSerializerProvider.findFactory(context: TypeSerializerContext): TypeSerializerFactory? {
	return factories.firstOrNull { it.canHandle(context) }
}

internal fun normalizeType(type: Class<*>): Class<*> {
	return when (type) {
		java.lang.Boolean.TYPE -> java.lang.Boolean::class.java
		java.lang.Byte.TYPE -> java.lang.Byte::class.java
		java.lang.Short.TYPE -> java.lang.Short::class.java
		java.lang.Integer.TYPE -> java.lang.Integer::class.java
		java.lang.Long.TYPE -> java.lang.Long::class.java
		java.lang.Float.TYPE -> java.lang.Float::class.java
		java.lang.Double.TYPE -> java.lang.Double::class.java
		java.lang.Character.TYPE -> java.lang.Character::class.java
		java.lang.Void.TYPE -> java.lang.Void::class.java
		else -> type
	}
}