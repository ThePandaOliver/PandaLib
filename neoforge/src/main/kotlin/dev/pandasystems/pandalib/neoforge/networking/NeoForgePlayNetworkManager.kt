package dev.pandasystems.pandalib.neoforge.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.MinecraftRuntime
import dev.pandasystems.pandalib.core.RuntimeEnvironment
import dev.pandasystems.pandalib.core.handles.Identifier
import dev.pandasystems.pandalib.core.lifecycles.ServerLifecycle
import dev.pandasystems.pandalib.networking.*
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.client.network.ClientPacketDistributor
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar

@AutoService(PlayPlayNetworkManager::class)
class NeoForgePlayNetworkManager : PlayPlayNetworkManager {
	override fun <T> sendToServer(type: PacketType<T>, value: T) {
		checkRegistered(type, PacketDirection.CLIENT_TO_SERVER)
		check(MinecraftRuntime.environment == RuntimeEnvironment.CLIENT) {
			"sendToServer can only be called on a client."
		}
		ClientPacketDistributor.sendToServer(payload(type, value))
	}

	override fun <T> sendToPlayer(player: Player, type: PacketType<T>, value: T) {
		checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)
		check(MinecraftRuntime.environment == RuntimeEnvironment.SERVER) {
			"sendToPlayer can only be called on a server."
		}
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
			val peer = player
			if (filter(peer)) PacketDistributor.sendToPlayer(player, payload(type, value))
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
		packetTypes[type.id] = type

		val registrationAction: (PayloadRegistrar) -> Unit = { reg ->
			registerPayload(reg, type, handler)
		}

		val currentRegistrar = registrar
		if (currentRegistrar != null) {
			registrationAction(currentRegistrar)
		} else {
			synchronized(queuedRegistrations) {
				queuedRegistrations.add(registrationAction)
			}
		}
	}

	@Suppress("UNCHECKED_CAST")
	private fun <T> registerPayload(
		reg: PayloadRegistrar,
		type: PacketType<T>,
		handler: PlayPacketHandler<T>
	) {
		val payloadType = NeoForgePacketPayload.type(type.id)
		val payloadCodec = NeoForgePacketPayload.codec(payloadType) as StreamCodec<FriendlyByteBuf, NeoForgePacketPayload>

		when (type.direction) {
			PacketDirection.CLIENT_TO_SERVER -> {
				reg.configurationToServer(payloadType, payloadCodec) { payload, context ->
					handler.handle(
						PlayPacketContextImpl(
							player = context.player(),
							executor = { task -> context.enqueueWork { task() } },
							sender = this,
							replyToServer = false
						),
						type.codec.decode(payload.data)
					)
				}
			}
			PacketDirection.SERVER_TO_CLIENT -> {
				reg.configurationToClient(payloadType, payloadCodec) { payload, context ->
					handler.handle(
						PlayPacketContextImpl(
							player = context.player(),
							executor = { task -> context.enqueueWork { task() } },
							sender = this,
							replyToServer = true
						),
						type.codec.decode(payload.data)
					)
				}
			}
		}
	}

	companion object {
		private val packetTypes = mutableMapOf<Identifier, PacketType<*>>()

		private var registrar: PayloadRegistrar? = null
		private val queuedRegistrations = mutableListOf<(PayloadRegistrar) -> Unit>()

		private val server: MinecraftServer? get() = ServerLifecycle.instance

		internal fun registrationEvent(event: RegisterPayloadHandlersEvent) {
			val reg = event.registrar("1")
			registrar = reg
			synchronized(queuedRegistrations) {
				queuedRegistrations.forEach { it(reg) }
				queuedRegistrations.clear()
			}
		}

		private fun <T> checkRegistered(type: PacketType<T>, expectedDirection: PacketDirection) {
			require(type.direction == expectedDirection) {
				"Packet '${type.id}' has direction ${type.direction}; expected $expectedDirection."
			}
			require(type.phase == NetworkPhase.PLAY) {
				"Packet '${type.id}' has phase ${type.phase}; expected ${NetworkPhase.PLAY}."
			}
			check(packetTypes[type.id] === type) {
				"Packet '${type.id}' must be registered before it can be sent."
			}
		}

		private fun <T> payload(type: PacketType<T>, value: T): NeoForgePacketPayload =
			NeoForgePacketPayload(
				NeoForgePacketPayload.type(
					type.id
				), type.codec.encode(value)
			)
	}
}
