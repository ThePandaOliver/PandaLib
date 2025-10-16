/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.options

import com.google.common.reflect.TypeToken
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import dev.pandasystems.pandalib.config.ConfigObject
import java.lang.reflect.Field
import java.lang.reflect.Type
import java.util.function.Supplier
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.kotlinProperty

class GenericConfigOption<T>(
	configObject: ConfigObject<*>, pathName: String, type: TypeToken<T>,
	override var value: T
) : ConfigOption<T>(configObject, pathName, type)