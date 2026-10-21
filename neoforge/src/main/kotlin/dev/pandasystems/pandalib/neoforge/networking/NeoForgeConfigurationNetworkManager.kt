package dev.pandasystems.pandalib.neoforge.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.handles.Identifier
import dev.pandasystems.pandalib.networking.*
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar

@AutoService(ConfigurationNetworkManager::class)
class NeoForgeConfigurationNetworkManager : ConfigurationNetworkManager {
	override fun <T> sendToClient(
		listener: ServerConfigurationPacketListener,
		type: PacketType<T>,
		value: T
	) {
		checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)
		listener.send(ClientboundCustomPayloadPacket(payload(type, value)))
	}

	@Suppress("UNCHECKED_CAST")
	override fun <T> registerPayload(type: PacketType<T>) {
		require(type.id !in packetTypes) {
			"A configuration packet is already registered with id '${type.id}'."
		}
		require(type.phase == NetworkPhase.CONFIGURATION) {
			"Packet '${type.id}' has phase ${type.phase}; expected ${NetworkPhase.CONFIGURATION}."
		}
		require(type.direction == PacketDirection.SERVER_TO_CLIENT) {
			"Packet '${type.id}' has direction ${type.direction}; expected ${PacketDirection.SERVER_TO_CLIENT}."
		}
		packetTypes[type.id] = type

		enqueueRegistration { reg ->
			val payloadType = NeoForgePacketPayload.type(type.id)
			val payloadCodec = NeoForgePacketPayload.codec(payloadType) as StreamCodec<FriendlyByteBuf, NeoForgePacketPayload>
			reg.configurationToClient(payloadType, payloadCodec)
		}
	}

	@Suppress("UNCHECKED_CAST")
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
		require(type.direction == PacketDirection.CLIENT_TO_SERVER) {
			"Packet '${type.id}' has direction ${type.direction}; expected ${PacketDirection.CLIENT_TO_SERVER}. Use ClientConfigurationNetworkManager.register for SERVER_TO_CLIENT handlers."
		}
		packetTypes[type.id] = type

		enqueueRegistration { reg ->
			val payloadType = NeoForgePacketPayload.type(type.id)
			val payloadCodec = NeoForgePacketPayload.codec(payloadType) as StreamCodec<FriendlyByteBuf, NeoForgePacketPayload>
			reg.configurationToServer(payloadType, payloadCodec) { payload, context ->
				handler.handle(
					ConfigurationPacketContextImpl(
						listener = context.listener() as ServerConfigurationPacketListener,
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
			require(type.phase == NetworkPhase.CONFIGURATION) {
				"Packet '${type.id}' has phase ${type.phase}; expected ${NetworkPhase.CONFIGURATION}."
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
