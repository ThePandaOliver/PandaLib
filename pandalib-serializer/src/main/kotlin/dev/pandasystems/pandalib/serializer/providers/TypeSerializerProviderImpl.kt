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

import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializerFactory

class TypeSerializerProviderImpl : TypeSerializerProvider {
	override val typeSerializers: MutableMap<Class<*>, () -> TypeSerializer<*>> = mutableMapOf()
	override val factories: MutableList<TypeSerializerFactory> = mutableListOf()
	
	override fun <Type : Any> register(
		type: Class<out Type>,
		serializerFactory: () -> TypeSerializer<out Type>
	) {
		typeSerializers[normalizeType(type)] = serializerFactory
	}

	override fun register(factory: TypeSerializerFactory) {
		factories += factory
	}
}