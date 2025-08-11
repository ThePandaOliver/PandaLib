/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

@file:JvmName("Services")

package dev.pandasystems.pandalib.core.platform

import dev.pandasystems.pandalib.core.logger
import java.util.*

@JvmField
val deferredRegisterHelper = load(DeferredRegisterHelper::class.java)

@JvmField
val resourceLoaderHelper = load(ResourceLoaderHelper::class.java)

@JvmField
val rendererRegistrationHelper = load(RendererRegistrationHelper::class.java)

@JvmField
val game = load(GameHelper::class.java)

@JvmField
val modLoader = load(ModLoaderHelper::class.java)

@JvmField
val registryRegistrations = load(RegistryRegistrations::class.java)

private fun <T> load(serviceClass: Class<T>): T {
	val loadedService = ServiceLoader.load(serviceClass).findFirst()
		.orElseThrow { NullPointerException("Failed to load service for " + serviceClass.name) }
	logger.debug("Loaded {} for service {}", loadedService, serviceClass)
	return loadedService
}