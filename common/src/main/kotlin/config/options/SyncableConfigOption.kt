/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.options

import com.google.gson.JsonElement
import dev.pandasystems.pandalib.config.ConfigObject
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.logger
import net.minecraft.world.entity.player.Player
import java.lang.reflect.Field
import java.util.UUID

class SyncableConfigOption<T: Any>(
	val delegate: ConfigOption<T>
) : ConfigOption<T>() {
	// The player specific values that is synced to the server from the client.
	internal var syncedPlayerValues: MutableMap<UUID, T> = mutableMapOf()

	// The value that is synced to the client from the server.
	internal var syncedValue: T? = null // TODO: Set to null when player leaves the server

	override var value: T
		get() = syncedValue ?: initialValue
		set(value) {
			delegate.value = value
		}

	val initialValue: T get() = delegate.value

	override fun initialize(configObject: ConfigObject<*>, path: String, name: String) {
		super.initialize(configObject, path, name)
		delegate.initialize(configObject, path, name)

		@Suppress("UNCHECKED_CAST")
		ConfigSynchronizer.registerSyncableConfigOption(this as SyncableConfigOption<Any>)
	}

	override fun serialize(): JsonElement = delegate.serialize()
	override fun deserialize(element: JsonElement): T = delegate.deserialize(element)

	operator fun get(player: UUID): T {
		var syncedValue = syncedPlayerValues[player]
		if (syncedValue == null) {
			logger.warn("No synced value for player $player in config option ${delegate.path}")
			syncedValue = initialValue
		}
		return syncedValue!!
	}

	operator fun get(player: Player): T = this[player.uuid]
}

fun <T: Any> ConfigOption<T>.syncable() = SyncableConfigOption(this)