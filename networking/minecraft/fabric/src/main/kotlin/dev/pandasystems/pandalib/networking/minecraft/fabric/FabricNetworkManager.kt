package dev.pandasystems.pandalib.networking.minecraft.fabric

import dev.pandasystems.pandalib.networking.NetworkPeer
import dev.pandasystems.pandalib.networking.NetworkSource
import dev.pandasystems.pandalib.networking.PacketDirection
import dev.pandasystems.pandalib.networking.PacketHandler
import dev.pandasystems.pandalib.networking.PacketId
import dev.pandasystems.pandalib.networking.PacketType
import dev.pandasystems.pandalib.networking.minecraft.MinecraftNetworkPeer
import dev.pandasystems.pandalib.networking.minecraft.MinecraftPacketContext
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.Identifier
import net.minecraft.server.MinecraftServer
import kotlin.collections.set

class FabricNetworkManager : NetworkSource {
    val peers = mutableSetOf<NetworkPeer>()

    private val packetTypes = mutableMapOf<PacketId, PacketType<*>>()

    @Volatile
    private var server: MinecraftServer? = null

    init {
        // Create and Delete the network peer representing the player
        ServerPlayerEvents.JOIN.register { peers.add(MinecraftNetworkPeer(it)) }
        ServerPlayerEvents.LEAVE.register { player -> peers.removeIf { player.stringUUID == it.id } }

        // Get and lose the server reference
        ServerLifecycleEvents.SERVER_STARTED.register { server = it }
        ServerLifecycleEvents.SERVER_STOPPED.register { stoppedServer ->
            if (server === stoppedServer) server = null
        }
    }

    override fun <T> sendToServer(type: PacketType<T>, value: T) {
        TODO("Not yet implemented")
    }

    override fun <T> sendToPeer(
        peer: NetworkPeer,
        type: PacketType<T>,
        value: T
    ) {
        TODO("Not yet implemented")
    }

    override fun <T> broadcast(
        type: PacketType<T>,
        value: T,
        filter: (NetworkPeer) -> Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun <T> register(
        type: PacketType<T>,
        handler: PacketHandler<T>
    ) {
        require(type.id !in packetTypes) {
            "A packet is already registered with id '${type.id}'."
        }

        val payloadType = FabricPacketPayload.type(type.id.toIdentifier())
        val payloadCodec = FabricPacketPayload.codec(payloadType)
        when (type.direction) {
            PacketDirection.CLIENT_TO_SERVER -> {
                PayloadTypeRegistry.serverboundPlay().register(payloadType, payloadCodec)
                check(ServerPlayNetworking.registerGlobalReceiver(payloadType) { payload, context ->
                    handler.handle(
                        MinecraftPacketContext(
                            peer = MinecraftNetworkPeer(context.player()),
                            executor = { task -> context.server().execute(task) },
                            sender = this,
                            replyToServer = false,
                        ),
                        type.codec.decode(payload.data),
                    )
                }) {
                    "Fabric already has a serverbound receiver for packet '${type.id}'."
                }
            }

            PacketDirection.SERVER_TO_CLIENT -> {
                PayloadTypeRegistry.clientboundPlay().register(payloadType, payloadCodec)
                if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
                    check(ClientPlayNetworking.registerGlobalReceiver(payloadType) { payload, context ->
                        handler.handle(
                            MinecraftPacketContext(
                                peer = MinecraftNetworkPeer(context.player()),
                                executor = { task -> context.client().execute(task) },
                                sender = this,
                                replyToServer = true,
                            ),
                            type.codec.decode(payload.data),
                        )
                    }) {
                        "Fabric already has a clientbound receiver for packet '${type.id}'."
                    }
                }
            }
        }
        packetTypes[type.id] = type
    }
}

internal fun PacketId.toIdentifier(): Identifier {
    require(':' in value) {
        "Packet id '$value' must be namespaced, for example 'examplemod:sync'."
    }
    return try {
        Identifier.parse(value)
    } catch (exception: IllegalArgumentException) {
        throw IllegalArgumentException(
            "Packet id '$value' is not a valid Minecraft identifier. Use a namespaced id such as 'examplemod:sync'.",
            exception,
        )
    }
}