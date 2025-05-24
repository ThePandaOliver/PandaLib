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
package me.pandamods.pandalib.fabric.platform.utils

import dev.architectury.utils.Env
import me.pandamods.pandalib.networking.NetworkContext
import me.pandamods.pandalib.networking.NetworkReceiver
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

object ClientPlayPayloadHandler {
	fun <T : CustomPacketPayload> receivePlay(receiver: NetworkReceiver<T>, payload: T, context: ClientPlayNetworking.Context) {
		val networkContext = NetworkContext(context.player(), Env.CLIENT)
		receiver.receive(networkContext, payload)
	}
}