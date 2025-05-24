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

import dev.architectury.platform.Platform
import dev.architectury.utils.Env
import me.pandamods.pandalib.config.Config
import me.pandamods.pandalib.config.ConfigData
import net.minecraft.world.entity.player.Player
import java.util.*

class ClientConfigHolder<T : ConfigData>(configClass: Class<T>, config: Config) : ConfigHolder<T>(configClass, config) {
	private val configs: MutableMap<UUID?, T> = HashMap<UUID?, T>()

	public override fun save() {
		if (Platform.getEnvironment() == Env.CLIENT) {
			super.save()
		} else this.logger.warn("Client config '{}' can't be saved on server", this.definition.name)
	}

	public override fun load(): Boolean {
		if (Platform.getEnvironment() == Env.CLIENT) {
			return super.load()
		}
		return false
	}

	fun <C : ConfigData?> putConfig(player: Player, config: C?) {
		configs.put(player.getUUID(), config as T)
	}

	fun getConfig(player: Player): T? {
		if (configs.containsKey(player.getUUID())) return configs[player.getUUID()]
		return this.get()
	}
}
