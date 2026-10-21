package dev.pandasystems.pandalib.fabric.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.handles.Identifier
import dev.pandasystems.pandalib.core.lifecycles.ServerLifecycle
import dev.pandasystems.pandalib.networking.*
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

@AutoService(PlayPlayNetworkManager::class)
class FabricPlayNetworkManager : PlayPlayNetworkManager {
    private val server: MinecraftServer? get() = ServerLifecycle.instance

    override fun <T> sendToPlayer(player: Player, type: PacketType<T>, value: T) {
        checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)
        ServerPlayNetworking.send(player as ServerPlayer, payload(type, value))
    }

    override fun <T> broadcast(
        type: PacketType<T>,
        value: T,
        filter: (Player) -> Boolean,
    ) {
        checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)
        val currentServer = checkNotNull(server) {
            "broadcast can only be called while a Minecraft server is running."
        }
        currentServer.playerList.players.forEach { player ->
            if (filter(player)) ServerPlayNetworking.send(player, payload(type, value))
        }
    }

    override fun <T> registerPayload(type: PacketType<T>) {
        require(type.id !in packetTypes) {
            "A packet is already registered with id '${type.id}'."
        }
        require(type.phase == NetworkPhase.PLAY) {
            "Packet '${type.id}' has phase ${type.phase}; expected ${NetworkPhase.PLAY}."
        }
        require(type.direction == PacketDirection.SERVER_TO_CLIENT) {
            "Packet '${type.id}' has direction ${type.direction}; expected ${PacketDirection.SERVER_TO_CLIENT}."
        }

        val payloadType = FabricPacketPayload.type(type.id)
        val payloadCodec = FabricPacketPayload.codec(payloadType)
        PayloadTypeRegistry.clientboundPlay().register(payloadType, payloadCodec)
        packetTypes[type.id] = type
    }

    override fun <T> register(
        type: PacketType<T>,
        handler: PlayPacketHandler<T>
    ) {
        require(type.id !in packetTypes) {
            "A packet is already registered with id '${type.id}'."
        }
        require(type.phase == NetworkPhase.PLAY) {
            "Packet '${type.id}' has phase ${type.phase}; expected ${NetworkPhase.PLAY}."
        }
        require(type.direction == PacketDirection.CLIENT_TO_SERVER) {
            "Packet '${type.id}' has direction ${type.direction}; expected ${PacketDirection.CLIENT_TO_SERVER}. Use ClientPlayNetworkManager.register for SERVER_TO_CLIENT handlers."
        }

        val payloadType = FabricPacketPayload.type(type.id)
        val payloadCodec = FabricPacketPayload.codec(payloadType)
        PayloadTypeRegistry.serverboundPlay().register(payloadType, payloadCodec)
        check(ServerPlayNetworking.registerGlobalReceiver(payloadType) { payload, context ->
            handler.handle(
                PlayPacketContextImpl(
                    player = context.player(),
                    executor = { task -> context.server().execute(task) },
                    sender = this,
                ),
                type.codec.decode(payload.data),
            )
        }) {
            "Fabric already has a serverbound receiver for packet '${type.id}'."
        }
        packetTypes[type.id] = type
    }

    companion object {
        private val packetTypes = mutableMapOf<Identifier, PacketType<*>>()

        internal fun <T> checkRegistered(type: PacketType<T>, expectedDirection: PacketDirection) {
            require(type.direction == expectedDirection) {
                "Packet '${type.id}' has direction ${type.direction}; expected $expectedDirection."
            }
            require(type.phase == NetworkPhase.PLAY) {
                "Packet '${type.id}' has phase ${type.phase}; expected ${NetworkPhase.PLAY}."
            }
            check(packetTypes[type.id] === type) {
                "Packet '${type.id}' must be registered before it can be used."
            }
        }

        internal fun <T> payload(type: PacketType<T>, value: T): FabricPacketPayload =
            FabricPacketPayload(FabricPacketPayload.type(type.id), type.codec.encode(value))
    }
}
