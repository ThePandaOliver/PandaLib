/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.config.options

import dev.pandasystems.pandalib.config.ConfigObject
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.pandalibLogger
import dev.pandasystems.pandalib.utils.gameEnvironment
import net.minecraft.world.entity.player.Player
import java.util.*
import kotlin.reflect.KType

class SyncableConfigOption<T : Any>(
	configObject: ConfigObject<*>, pathName: String, type: KType, value: T
) : ConfigOption<T>(configObject, pathName, type) {
	// The player specific values that is synced to the server from the client.
	internal var playerValues = mutableMapOf<UUID, T>()

	// The value that is synced to the client from the server.
	internal var serverValue: T? = null
		get() = if (gameEnvironment.isHost) initialValue else field

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
		pandalibLogger.warn("No synced value for player $player in config option $pathName")
		return initialValue
	}

	operator fun get(player: Player): T = this[player.uuid]
}