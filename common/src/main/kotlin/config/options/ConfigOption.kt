/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.options

import com.google.common.reflect.TypeToken
import dev.pandasystems.pandalib.config.ConfigObject
import java.lang.reflect.Type

abstract class ConfigOption<T>(
	val configObject: ConfigObject<*>,
	val pathName: String,
	val type: TypeToken<T>
) {
	val name: String = pathName.substringAfterLast('.')

	abstract var value: T
}