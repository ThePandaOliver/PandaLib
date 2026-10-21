package dev.pandasystems.pandalib.fabric.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.handles.Identifier
import dev.pandasystems.pandalib.networking.*
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl

@AutoService(ConfigurationNetworkManager::class)
class FabricConfigurationNetworkManager : ConfigurationNetworkManager {
	override fun <T> sendToClient(
		listener: ServerConfigurationPacketListener,
		type: PacketType<T>,
		value: T
	) {
		checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)
		ServerConfigurationNetworking.send(listener as ServerConfigurationPacketListenerImpl, payload(type, value))
	}

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

		val payloadType = FabricPacketPayload.type(type.id)
		val payloadCodec = FabricPacketPayload.codec(payloadType)
		PayloadTypeRegistry.clientboundConfiguration().register(payloadType, payloadCodec)
		packetTypes[type.id] = type
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
		require(type.direction == PacketDirection.CLIENT_TO_SERVER) {
			"Packet '${type.id}' has direction ${type.direction}; expected ${PacketDirection.CLIENT_TO_SERVER}. Use ClientConfigurationNetworkManager.register for SERVER_TO_CLIENT handlers."
		}

		val payloadType = FabricPacketPayload.type(type.id)
		val payloadCodec = FabricPacketPayload.codec(payloadType)
		PayloadTypeRegistry.serverboundConfiguration().register(payloadType, payloadCodec)
		check(ServerConfigurationNetworking.registerGlobalReceiver(payloadType) { payload, context ->
			handler.handle(
				ConfigurationPacketContextImpl(
					listener = context.packetListener(),
					executor = { task -> context.server().execute(task) },
					sender = this,
				),
				type.codec.decode(payload.data),
			)
		}) {
			"Fabric already has a serverbound configuration receiver for packet '${type.id}'."
		}
		packetTypes[type.id] = type
	}

	companion object {
		private val packetTypes = mutableMapOf<Identifier, PacketType<*>>()

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

		internal fun <T> payload(type: PacketType<T>, value: T): FabricPacketPayload =
			FabricPacketPayload(FabricPacketPayload.type(type.id), type.codec.encode(value))
	}
}
