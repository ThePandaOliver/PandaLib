
package dev.pandasystems.pandalib.fabric.platform.utils

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.networking.NetworkContext
import dev.pandasystems.pandalib.networking.NetworkReceiver
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

object ClientPlayPayloadHandler {
	fun <T : CustomPacketPayload> receivePlay(receiver: NetworkReceiver<T>, payload: T, context: ClientPlayNetworking.Context) {
		val networkContext = NetworkContext(context.player(), Env.CLIENT)
		receiver.receive(networkContext, payload)
	}
}