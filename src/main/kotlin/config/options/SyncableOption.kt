/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.config.options

import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.config.OptionContainer
import dev.pandasystems.pandalib.pandalibLogger
import dev.pandasystems.pandalib.utils.gameEnvironment
import net.minecraft.world.entity.player.Player
import java.util.*
import kotlin.reflect.KType

class SyncableOption<T : Any?>(value: T, valueType: KType, name: String, parent: OptionContainer) : ConfigOption<T>(value, valueType, name, parent) {
	// The player specific values that is synced to the server from the client.
	var playerValues = mutableMapOf<UUID, T>()

	// The value that is synced to the client from the server.
	var serverValue: T? = null
		get() = if (gameEnvironment.isHost) value else field

	init {
		@Suppress("UNCHECKED_CAST")
		ConfigSynchronizer.registerSyncableConfigOption(this as SyncableOption<Any?>)
	}

	operator fun get(player: UUID): T {
		val playerValue = playerValues[player]
		if (playerValue != null) return playerValue
		pandalibLogger.warn("No synced value for player $player in config option $path")
		return value
	}

	operator fun get(player: Player): T = this[player.uuid]
}

inline fun <reified T : Any?> OptionContainer.synced(value: T): ConfigOptionDelegate<T, SyncableOption<T>> =
	ConfigOptionDelegate(value, ::SyncableOption)