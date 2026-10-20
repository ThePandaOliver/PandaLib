package dev.pandasystems.pandalib.neoforge.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.MinecraftRuntime
import dev.pandasystems.pandalib.core.RuntimeEnvironment
import dev.pandasystems.pandalib.core.handles.Identifier
import dev.pandasystems.pandalib.networking.*
import dev.pandasystems.pandalib.registry.DeferredRegistry
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl
import net.neoforged.neoforge.client.network.ClientPacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar

@AutoService(ConfigurationNetworkManager::class)
class NeoForgeConfigurationNetworkManager : ConfigurationNetworkManager {
	override fun <T> sendToServer(type: PacketType<T>, value: T) {
		checkRegistered(type, PacketDirection.CLIENT_TO_SERVER)
		check(MinecraftRuntime.environment == RuntimeEnvironment.CLIENT) {
			"sendToServer can only be called on a client."
		}
		ClientPacketDistributor.sendToServer(payload(type, value))
	}

	override fun <T> sendToClient(
		listener: ServerConfigurationPacketListener,
		type: PacketType<T>,
		value: T
	) {
		checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)
		check(MinecraftRuntime.environment == RuntimeEnvironment.SERVER) {
			"sendToClient can only be called on a server."
		}
		listener.send(ClientboundCustomPayloadPacket(payload(type, value)))
	}

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
		handler: ConfigurationPacketHandler<T>
	) {
		val payloadType = NeoForgePacketPayload.type(type.id)
		val payloadCodec = NeoForgePacketPayload.codec(payloadType) as StreamCodec<FriendlyByteBuf, NeoForgePacketPayload>

		when (type.direction) {
			PacketDirection.CLIENT_TO_SERVER -> {
				reg.configurationToServer(payloadType, payloadCodec) { payload, context ->
					handler.handle(
						ConfigurationPacketContextImpl(
							listener = context.listener() as? ServerConfigurationPacketListenerImpl,
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
						ConfigurationPacketContextImpl(
							listener = null,
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
			require(type.phase == NetworkPhase.CONFIGURATION) {
				"Packet '${type.id}' has phase ${type.phase}; expected ${NetworkPhase.CONFIGURATION}."
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
