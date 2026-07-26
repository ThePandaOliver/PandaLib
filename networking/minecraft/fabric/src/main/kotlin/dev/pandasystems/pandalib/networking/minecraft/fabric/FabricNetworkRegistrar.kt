package dev.pandasystems.pandalib.networking.minecraft.fabric

import dev.pandasystems.pandalib.networking.NetworkRegistrar
import dev.pandasystems.pandalib.networking.PacketHandler
import dev.pandasystems.pandalib.networking.PacketId
import dev.pandasystems.pandalib.networking.PacketType
import dev.pandasystems.pandalib.networking.minecraft.MinecraftNetworkPeer
import dev.pandasystems.pandalib.networking.minecraft.MinecraftPacketContext
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class FabricNetworkRegistrar : NetworkRegistrar {
    private val packetTypes = mutableMapOf<PacketId, PacketType<*>>()

    override fun <T> register(
        type: PacketType<T>,
        handler: PacketHandler<T>
    ) {
        packetTypes[type.id] = type
        ClientPlayNetworking.registerGlobalReceiver(type.id.toPayloadId(), PacketHandlerFabric(handler))
    }

    private fun PacketId.toPayloadId() = CustomPacketPayload.Type<FabricPacketPayload>(Identifier.fromNamespaceAndPath("pandalib", this.value))

    inner class PacketHandlerFabric<T>(
        val handler: PacketHandler<T>
    ) : ClientPlayNetworking.PlayPayloadHandler<FabricPacketPayload> {
        override fun receive(
            payload: FabricPacketPayload,
            context: ClientPlayNetworking.Context
        ) {
            val id = payload.id
            @Suppress("UNCHECKED_CAST")
            val type = packetTypes.getValue(PacketId(id.path)) as PacketType<out T>
            val codec = type.codec
            val packet = codec.decode(payload.data)

            val newContext = MinecraftPacketContext(
                peer = MinecraftNetworkPeer(context.player()),
                executor = { task -> context.client().execute(task) },
                sender = FabricPacketSender(context.responseSender()),
            )
            handler.handle(newContext, packet)
        }
    }
}