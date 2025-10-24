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

package dev.pandasystems.pandalib.config.datatypes

import java.util.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class Synced<T>(
	override var value: T,
	override val valueType: KType
) : ConfigDataType<T> {
	val serverValue: T = value
	val playerValues = mapOf<UUID, T>()

	companion object {
		inline operator fun <reified T> invoke(value: T): Synced<T> {
			return Synced(value, typeOf<T>())
		}
	}
}