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
package me.pandamods.pandalib.networking

import me.pandamods.pandalib.platform.Services
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

object PacketDistributor {
	@JvmStatic
	fun <T : CustomPacketPayload> sendToServer(payload: T) {
		Services.NETWORK.sendToServer<T>(payload)
	}

	@JvmStatic
	fun <T : CustomPacketPayload> sendToPlayer(player: ServerPlayer, payload: T) {
		Services.NETWORK.sendToPlayer<T>(player, payload)
	}

	@JvmStatic
	fun <T : CustomPacketPayload> sendToAllPlayers(payload: T) {
		Services.NETWORK.sendToAllPlayers<T>(payload)
	}
}
