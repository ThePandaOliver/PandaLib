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

package dev.pandasystems.pandalib.serializer

import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer
import java.lang.reflect.Field
import kotlin.reflect.KProperty

class PropertySerializerRegistry {
    private val propertySerializers: MutableMap<String, TypeSerializer<*>> = mutableMapOf()

    fun <Type : Any> register(
        propertyName: String,
        serializer: TypeSerializer<Type>
    ) {
        propertySerializers[propertyName] = serializer
    }

    fun <Type : Any> register(
        property: KProperty<Type>,
        serializer: TypeSerializer<Type>
    ) {
        propertySerializers[property.name] = serializer
    }

    fun find(propertyName: String): TypeSerializer<*>? {
        return propertySerializers[propertyName]
    }
}
