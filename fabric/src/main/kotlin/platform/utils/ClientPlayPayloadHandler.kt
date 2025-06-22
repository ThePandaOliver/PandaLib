/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform.utils

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.impl.networking.NetworkContext
import dev.pandasystems.pandalib.impl.networking.PacketHandler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

object ClientPlayPayloadHandler {
	fun <T : CustomPacketPayload> receivePlay(receiver: PacketHandler<T>, payload: T, context: ClientPlayNetworking.Context) {
		val networkContext = NetworkContext(context.player(), Env.CLIENT)
		receiver.handler(networkContext, payload)
	}
}