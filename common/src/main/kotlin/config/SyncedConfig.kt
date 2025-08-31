/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.event.serverevents.serverPlayerJoinEvent
import java.util.*

object SyncedConfig {
	private val clientConfigs = mutableMapOf<Class<*>, Any>()
	private val serverConfigs = mutableMapOf<Class<*>, Any>()

	@JvmStatic
	fun registerClient(holder: ConfigHolder<Any>) {
		clientConfigs[holder.configClass] = holder
	}

	@JvmStatic
	fun registerServer(holder: ConfigHolder<Any>) {
		serverConfigs[holder.configClass] = holder
	}


	private val syncedClientConfigs = mutableMapOf<Class<*>, MutableMap<UUID, Any>>()
	private val syncedServerConfigs = mutableMapOf<Class<*>, Any>()

	@JvmStatic
	fun <T : Any> getPlayerConfig(configClass: Class<T>, uuid: UUID): T {
		@Suppress("UNCHECKED_CAST")
		return syncedClientConfigs.computeIfAbsent(configClass) { mutableMapOf() }[uuid] as T
	}

	inline fun <reified T : Any> getPlayerConfig(uuid: UUID): T = getPlayerConfig(T::class.java, uuid)

	@JvmStatic
	fun <T : Any> getServerConfig(configClass: Class<T>): T {
		@Suppress("UNCHECKED_CAST")
		return syncedServerConfigs[configClass] as T
	}

	inline fun <reified T : Any> getServerConfig(): T = getServerConfig(T::class.java)

	internal fun init() {
	}
}