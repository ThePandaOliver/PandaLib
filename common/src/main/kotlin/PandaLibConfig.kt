/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib

import dev.pandasystems.pandalib.config.createConfigBuilder
import dev.pandasystems.pandalib.config.properties.addGenericProperty
import dev.pandasystems.pandalib.utils.extensions.resourceLocation

val pandalibConfig = createConfigBuilder(resourceLocation("pandalib")) {
	addGenericProperty("commonDebug", "Enables debug output", false)

	createSubMenu("client") {
		addGenericProperty("clientDebug", "Enables debug output in the client side", false)
	}

	createSubMenu("server") {
		addGenericProperty("serverDebug", "Enables debug output in the server side", false)
	}
}