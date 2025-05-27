
package dev.pandasystems.pandalib.networking

import net.minecraft.network.protocol.common.custom.CustomPacketPayload

fun interface NetworkReceiver<T : CustomPacketPayload> {
	fun receive(ctx: NetworkContext, payload: T)
}
