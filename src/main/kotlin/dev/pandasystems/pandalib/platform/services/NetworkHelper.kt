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
package dev.pandasystems.pandalib.platform.services

import dev.pandasystems.pandalib.networking.NetworkRegistry
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

interface NetworkHelper : NetworkRegistry {
	fun <T : CustomPacketPayload> sendToServer(payload: T)

	fun <T : CustomPacketPayload> sendToPlayer(player: ServerPlayer, payload: T)

	fun <T : CustomPacketPayload> sendToAllPlayers(payload: T)
}
