package dev.pandasystems.pandalib.neoforge.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.handles.Identifier
import dev.pandasystems.pandalib.core.lifecycles.ServerLifecycle
import dev.pandasystems.pandalib.networking.*
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar

@AutoService(PlayPlayNetworkManager::class)
class NeoForgePlayNetworkManager : PlayPlayNetworkManager {
	override fun <T> sendToPlayer(player: Player, type: PacketType<T>, value: T) {
		checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)
		PacketDistributor.sendToPlayer(player as ServerPlayer, payload(type, value))
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
			if (filter(player)) PacketDistributor.sendToPlayer(player, payload(type, value))
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
		packetTypes[type.id] = type

		enqueueRegistration { reg ->
			val payloadType = NeoForgePacketPayload.type(type.id)
			val payloadCodec = NeoForgePacketPayload.codec(payloadType)
			reg.playToClient(payloadType, payloadCodec)
		}
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
		packetTypes[type.id] = type

		enqueueRegistration { reg ->
			val payloadType = NeoForgePacketPayload.type(type.id)
			val payloadCodec: StreamCodec<RegistryFriendlyByteBuf, NeoForgePacketPayload> = NeoForgePacketPayload.codec(payloadType)
			reg.playToServer(payloadType, payloadCodec) { payload, context ->
				handler.handle(
					PlayPacketContextImpl(
						player = context.player(),
						executor = { task -> context.enqueueWork { task() } },
						sender = this,
					),
					type.codec.decode(payload.data)
				)
			}
		}
	}

	companion object {
		private val packetTypes = mutableMapOf<Identifier, PacketType<*>>()

		private var registrar: PayloadRegistrar? = null
		private val queuedRegistrations = mutableListOf<(PayloadRegistrar) -> Unit>()

		private val server: MinecraftServer? get() = ServerLifecycle.instance

		private fun enqueueRegistration(action: (PayloadRegistrar) -> Unit) {
			val currentRegistrar = registrar
			if (currentRegistrar != null) {
				action(currentRegistrar)
			} else {
				synchronized(queuedRegistrations) {
					queuedRegistrations.add(action)
				}
			}
		}

		internal fun registrationEvent(event: RegisterPayloadHandlersEvent) {
			val reg = event.registrar("1")
			registrar = reg
			synchronized(queuedRegistrations) {
				queuedRegistrations.forEach { it(reg) }
				queuedRegistrations.clear()
			}
		}

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

		internal fun <T> payload(type: PacketType<T>, value: T): NeoForgePacketPayload =
			NeoForgePacketPayload(
				NeoForgePacketPayload.type(
					type.id
				), type.codec.encode(value)
			)
	}
}
