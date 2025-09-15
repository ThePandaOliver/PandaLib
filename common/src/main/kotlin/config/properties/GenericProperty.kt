/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.properties

import dev.pandasystems.pandalib.config.ConfigBuilder
import dev.pandasystems.pandalib.config.ConfigProperty

class GenericProperty<T>(name: String, comment: String, default: T) : ConfigProperty<T>(name, comment, default) {

}

fun <T> ConfigBuilder.addGenericProperty(name: String, comment: String, default: T) {
	addProperty(GenericProperty(name, comment, default))
}