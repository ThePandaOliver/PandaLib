/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.pandamods.pandalib.platform

import com.mojang.logging.LogUtils
import me.pandamods.pandalib.platform.services.GameHelper
import me.pandamods.pandalib.platform.services.ModLoaderHelper
import me.pandamods.pandalib.platform.services.NetworkHelper
import me.pandamods.pandalib.platform.services.RegistrationHelper
import org.slf4j.Logger
import java.util.*
import java.util.function.Supplier

@Suppress("unused")
object Services {
	private val LOGGER: Logger = LogUtils.getLogger()

	@JvmField
	val NETWORK: NetworkHelper = load(NetworkHelper::class.java)
	@JvmField
	val REGISTRATION: RegistrationHelper = load(RegistrationHelper::class.java)
	@JvmField
	val GAME: GameHelper = load(GameHelper::class.java)
	@JvmField
	val MOD_LOADER: ModLoaderHelper = load(ModLoaderHelper::class.java)

	private fun <T> load(serviceClass: Class<T>): T {
		val loadedService = ServiceLoader.load(serviceClass).findFirst()
			.orElseThrow { NullPointerException("Failed to load service for " + serviceClass.getName()) }
		LOGGER.debug("Loaded {} for service {}", loadedService, serviceClass)
		return loadedService
	}
}
