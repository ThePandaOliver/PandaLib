/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.options

import com.google.common.reflect.TypeToken
import dev.pandasystems.pandalib.config.ConfigObject
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.logger
import dev.pandasystems.pandalib.platform.game
import net.minecraft.world.entity.player.Player
import java.lang.reflect.Type
import java.util.UUID

class SyncableConfigOption<T>(
	configObject: ConfigObject<*>, pathName: String, type: TypeToken<T>, value: T
) : ConfigOption<T>(configObject, pathName, type) {
	// The player specific values that is synced to the server from the client.
	internal var playerValues = mutableMapOf<UUID, T>()

	// The value that is synced to the client from the server.
	internal var serverValue: T? = null
		get() = if (game.isHost) initialValue else field

	override var value: T
		get() = serverValue ?: initialValue
		set(value) {
			initialValue = value
		}

	var initialValue: T = value

	init {
		@Suppress("UNCHECKED_CAST")
		ConfigSynchronizer.registerSyncableConfigOption(this as SyncableConfigOption<Any>)
	}

	operator fun get(player: UUID): T {
		val playerValue = playerValues[player]
		if (playerValue != null) return playerValue
		logger.warn("No synced value for player $player in config option $pathName")
		return initialValue
	}

	operator fun get(player: Player): T = this[player.uuid]
}