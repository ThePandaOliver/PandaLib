/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.pandamods.pandalib.config.holders

import me.pandamods.pandalib.config.Config
import me.pandamods.pandalib.config.ConfigData
import me.pandamods.pandalib.platform.Services
import net.minecraft.world.entity.player.Player
import java.util.*

class ClientConfigHolder<T : ConfigData>(configClass: Class<T>, config: Config) : ConfigHolder<T>(configClass, config) {
	private val configs = mutableMapOf<UUID, T>()

	override fun save() {
		if (Services.GAME.isClient) {
			super.save()
		} else logger.warn("Client config '${definition.name}' can't be saved on server")
	}

	override fun load(): Boolean {
		if (Services.GAME.isClient) {
			return super.load()
		}
		return false
	}

	fun putConfig(player: Player, config: T) {
		configs.put(player.getUUID(), config)
	}

	fun getConfig(player: Player): T? {
		return configs[player.getUUID()]
	}
}
