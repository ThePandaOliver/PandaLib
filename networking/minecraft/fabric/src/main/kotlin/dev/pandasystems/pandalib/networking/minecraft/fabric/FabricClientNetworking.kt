package dev.pandasystems.pandalib.networking.minecraft.fabric

import dev.pandasystems.pandalib.networking.PacketHandler
import dev.pandasystems.pandalib.networking.PacketType
import dev.pandasystems.pandalib.networking.minecraft.MinecraftNetworkPeer
import dev.pandasystems.pandalib.networking.minecraft.MinecraftPacketContext
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

/** Keeps client-only Fabric API references out of the common registrar implementation. */
@Environment(EnvType.CLIENT)
internal object FabricClientNetworking {
    fun <T> registerReceiver(
        payloadType: CustomPacketPayload.Type<FabricPacketPayload>,
        type: PacketType<T>,
        handler: PacketHandler<T>,
        sender: FabricNetworkRegistrar,
    ): Boolean =
        ClientPlayNetworking.registerGlobalReceiver(payloadType) { payload, context ->
            handler.handle(
                MinecraftPacketContext(
                    peer = MinecraftNetworkPeer(context.player()),
                    executor = { task -> context.client().execute(task) },
                    sender = sender,
                    replyToServer = true,
                ),
                type.codec.decode(payload.data),
            )
        }

    fun sendToServer(payload: FabricPacketPayload) {
        ClientPlayNetworking.send(payload)
    }
}
