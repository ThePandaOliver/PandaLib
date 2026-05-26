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

package dev.pandasystems.pandalib.serializer.resolver

import dev.pandasystems.pandalib.serializer.TypeSerializerContext
import dev.pandasystems.pandalib.serializer.providers.TypeSerializerProvider
import dev.pandasystems.pandalib.serializer.providers.findDirect
import dev.pandasystems.pandalib.serializer.providers.findFactory
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer

class DefaultTypeSerializerResolver(
	private val localProvider: TypeSerializerProvider,
	private val globalProvider: TypeSerializerProvider
) : TypeSerializerResolver {
	override fun resolve(context: TypeSerializerContext): TypeSerializer<*>? {
		localProvider.findDirect(context.rawType)?.let {
			return it()
		}

		localProvider.findFactory(context)?.let {
			return it.create(context, this)
		}

		globalProvider.findDirect(context.rawType)?.let {
			return it()
		}

		globalProvider.findFactory(context)?.let {
			return it.create(context, this)
		}

		return null
	}
}