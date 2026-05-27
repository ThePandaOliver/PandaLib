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

import dev.pandasystems.pandalib.serializer.typeserializers.factories.EnumSerializerFactory
import dev.pandasystems.pandalib.serializer.typeserializers.types.LongSerializer
import dev.pandasystems.pandalib.serializer.typeserializers.types.primetives.*

object GlobalTypeSerializers : TypeSerializerProvider by TypeSerializerProviderImpl() {
	init {
		register(String::class.java, StringSerializer())

		register(Boolean::class.javaObjectType, BooleanSerializer())
		register(Boolean::class.javaPrimitiveType!!, BooleanSerializer())

		register(Int::class.javaObjectType, IntSerializer())
		register(Int::class.javaPrimitiveType!!, IntSerializer())

		register(Long::class.javaObjectType, LongSerializer())
		register(Long::class.javaPrimitiveType!!, LongSerializer())

		register(Float::class.javaObjectType, FloatSerializer())
		register(Float::class.javaPrimitiveType!!, FloatSerializer())

		register(Double::class.javaObjectType, DoubleSerializer())
		register(Double::class.javaPrimitiveType!!, DoubleSerializer())

		register(Char::class.javaObjectType, CharSerializer())
		register(Char::class.javaPrimitiveType!!, CharSerializer())

		register(EnumSerializerFactory())
	}
}