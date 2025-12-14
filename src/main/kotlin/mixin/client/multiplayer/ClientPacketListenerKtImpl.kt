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

package dev.pandasystems.pandalib.mixin.client.multiplayer

import dev.pandasystems.pandalib.event.client.clientPlayerJoinEvent
import dev.pandasystems.pandalib.event.client.clientPlayerRespawnEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.protocol.game.ClientboundLoginPacket
import net.minecraft.network.protocol.game.ClientboundRespawnPacket

class ClientPacketListenerKtImpl(val minecraft: Minecraft) {
	var oldPlayer: LocalPlayer? = null
	
	fun onLoginEvent() {
		clientPlayerJoinEvent.invoker(minecraft.player!!)
	}

	fun onRespawnPreEvent() {
		oldPlayer = minecraft.player
	}

	fun onRespawnPostEvent() {
		val newPlayer = minecraft.player
		clientPlayerRespawnEvent.invoker(oldPlayer!!, newPlayer!!)
	}
}