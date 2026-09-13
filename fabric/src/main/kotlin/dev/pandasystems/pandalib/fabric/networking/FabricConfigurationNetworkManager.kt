package dev.pandasystems.pandalib.fabric.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.MinecraftRuntime
import dev.pandasystems.pandalib.core.RuntimeEnvironment
import dev.pandasystems.pandalib.networking.*
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl

@AutoService(ConfigurationNetworkManager::class)
class FabricConfigurationNetworkManager : ConfigurationNetworkManager {
    private val packetTypes = mutableMapOf<PacketId, PacketType<*>>()

    override fun <T> sendToServer(type: PacketType<T>, value: T) {
        checkRegistered(type, PacketDirection.CLIENT_TO_SERVER)
        check(MinecraftRuntime.environment == RuntimeEnvironment.CLIENT) {
            "sendToServer can only be called on a client."
        }
        ClientConfigurationNetworking.send(payload(type, value))
    }

    override fun <T> sendToClient(
        listener: ServerConfigurationPacketListenerImpl,
        type: PacketType<T>,
        value: T
    ) {
        checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)
        ServerConfigurationNetworking.send(listener, payload(type, value))
    }

    private fun <T> checkRegistered(type: PacketType<T>, expectedDirection: PacketDirection) {
        require(type.direction == expectedDirection) {
            "Packet '${type.id}' has direction ${type.direction}; expected $expectedDirection."
        }
        require(type.phase == NetworkPhase.CONFIGURATION) {
            "Packet '${type.id}' has phase ${type.phase}; expected ${NetworkPhase.CONFIGURATION}."
        }
        check(packetTypes[type.id] === type) {
            "Packet '${type.id}' must be registered before it can be sent."
        }
    }

    private fun <T> payload(type: PacketType<T>, value: T): FabricPacketPayload =
        FabricPacketPayload(FabricPacketPayload.type(type.id.toIdentifier()), type.codec.encode(value))

    override fun <T> register(
        type: PacketType<T>,
        handler: ConfigurationPacketHandler<T>
    ) {
        require(type.id !in packetTypes) {
            "A configuration packet is already registered with id '${type.id}'."
        }
        require(type.phase == NetworkPhase.CONFIGURATION) {
            "Packet '${type.id}' has phase ${type.phase}; expected ${NetworkPhase.CONFIGURATION}."
        }

        val payloadType = FabricPacketPayload.type(type.id.toIdentifier())
        val payloadCodec = FabricPacketPayload.codec(payloadType)
        when (type.direction) {
            PacketDirection.CLIENT_TO_SERVER -> {
                PayloadTypeRegistry.serverboundConfiguration().register(payloadType, payloadCodec)
                check(ServerConfigurationNetworking.registerGlobalReceiver(payloadType) { payload, context ->
                    handler.handle(
                        ConfigurationPacketContextImpl(
                            listener = context.packetListener(),
                            executor = { task -> context.server().execute(task) },
                            sender = this,
                            replyToServer = false,
                        ),
                        type.codec.decode(payload.data),
                    )
                }) {
                    "Fabric already has a serverbound configuration receiver for packet '${type.id}'."
                }
            }

            PacketDirection.SERVER_TO_CLIENT -> {
                PayloadTypeRegistry.clientboundConfiguration().register(payloadType, payloadCodec)
                if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
                    check(ClientConfigurationNetworking.registerGlobalReceiver(payloadType) { payload, context ->
                        handler.handle(
                            ConfigurationPacketContextImpl(
                                listener = null,
                                executor = { task -> context.client().execute(task) },
                                sender = this,
                                replyToServer = true,
                            ),
                            type.codec.decode(payload.data),
                        )
                    }) {
                        "Fabric already has a clientbound configuration receiver for packet '${type.id}'."
                    }
                }
            }
        }
        packetTypes[type.id] = type
    }
}
