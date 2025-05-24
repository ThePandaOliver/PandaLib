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
package me.pandamods.pandalib.core.event

import dev.architectury.event.events.common.PlayerEvent
import me.pandamods.pandalib.core.network.ConfigNetworking
import net.minecraft.server.level.ServerPlayer

object EventHandler {
	fun init() {
		PlayerEvent.PLAYER_JOIN.register(PlayerEvent.PlayerJoin { obj: ServerPlayer -> onServerPlayerJoin(obj) })
	}

	private fun onServerPlayerJoin(serverPlayer: ServerPlayer) {
		ConfigNetworking.SyncCommonConfigs(serverPlayer)
	}
}
