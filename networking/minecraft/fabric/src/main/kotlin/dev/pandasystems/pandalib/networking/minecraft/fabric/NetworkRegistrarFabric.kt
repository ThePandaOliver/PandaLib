package dev.pandasystems.pandalib.networking.minecraft.fabric

import dev.pandasystems.pandalib.networking.NetworkRegistrar
import dev.pandasystems.pandalib.networking.PacketHandler
import dev.pandasystems.pandalib.networking.PacketId
import dev.pandasystems.pandalib.networking.PacketType
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class NetworkRegistrarFabric : NetworkRegistrar {
    override fun <T> register(
        type: PacketType<T>,
        handler: PacketHandler<T>
    ) {
        ClientPlayNetworking.registerGlobalReceiver(type.id.toPayloadId(), PacketHandlerFabric())
    }

    private fun PacketId.toPayloadId() = CustomPacketPayload.Type<CustomPacketPayload>(Identifier.fromNamespaceAndPath("pandalib", this.value))

    class PacketHandlerFabric<T>(
        handler: PacketHandler<T>
    ) : ClientPlayNetworking.PlayPayloadHandler<CustomPacketPayload> {
        override fun receive(
            payload: CustomPacketPayload,
            context: ClientPlayNetworking.Context
        ) {
        }
    }
}