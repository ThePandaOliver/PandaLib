package dev.pandasystems.pandalib.impl.networking

import net.minecraft.network.protocol.common.custom.CustomPacketPayload

fun interface NetworkReceiver<T : CustomPacketPayload> {
	fun receive(ctx: NetworkContext, payload: T)
}
