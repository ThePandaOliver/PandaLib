/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

@file:JvmName("Services")

package dev.pandasystems.pandalib.api.platform

import dev.pandasystems.pandalib.core.logger
import java.util.*

@JvmField
val registryHelper: RegistrationHelper = load(RegistrationHelper::class.java)

@JvmField
val game: GameHelper = load(GameHelper::class.java)

@JvmField
val modLoader: ModLoaderHelper = load(ModLoaderHelper::class.java)

private fun <T> load(serviceClass: Class<T>): T {
	val loadedService = ServiceLoader.load(serviceClass).findFirst()
		.orElseThrow { NullPointerException("Failed to load service for " + serviceClass.name) }
	logger.debug("Loaded {} for service {}", loadedService, serviceClass)
	return loadedService
}