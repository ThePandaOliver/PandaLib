package dev.pandasystems.pandalib.fabric.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.networking.*
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

@AutoService(ClientPlayNetworkManager::class)
class FabricClientPlayNetworkManager : ClientPlayNetworkManager {
    override fun <T> sendToServer(type: PacketType<T>, value: T) {
        FabricPlayNetworkManager.checkRegistered(type, PacketDirection.CLIENT_TO_SERVER)
        ClientPlayNetworking.send(FabricPlayNetworkManager.payload(type, value))
    }

    override fun <T> register(
        type: PacketType<T>,
        handler: ClientPlayPacketHandler<T>,
    ) {
        FabricPlayNetworkManager.checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)
        val payloadType = FabricPacketPayload.type(type.id)
        check(ClientPlayNetworking.registerGlobalReceiver(payloadType) { payload, context ->
            handler.handle(
                ClientPlayPacketContextImpl(
                    player = context.player(),
                    executor = { task -> context.client().execute(task) },
                    sender = this,
                ),
                type.codec.decode(payload.data),
            )
        }) {
            "Fabric already has a clientbound receiver for packet '${type.id}'."
        }
    }
}
