package dev.pandasystems.pandalib.neoforge.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.networking.*
import net.neoforged.neoforge.client.network.ClientPacketDistributor
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent

@AutoService(ClientConfigurationNetworkManager::class)
class NeoForgeClientConfigurationNetworkManager : ClientConfigurationNetworkManager {
	override fun <T> sendToServer(type: PacketType<T>, value: T) {
		NeoForgeConfigurationNetworkManager.checkRegistered(type, PacketDirection.CLIENT_TO_SERVER)
		ClientPacketDistributor.sendToServer(NeoForgeConfigurationNetworkManager.payload(type, value))
	}

	override fun <T> register(
		type: PacketType<T>,
		handler: ClientConfigurationPacketHandler<T>,
	) {
		NeoForgeConfigurationNetworkManager.checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)

		val action: (RegisterClientPayloadHandlersEvent) -> Unit = { event ->
			val payloadType = NeoForgePacketPayload.type(type.id)
			event.register(payloadType) { payload, context ->
				handler.handle(
					ClientConfigurationPacketContextImpl(
						executor = { task -> context.enqueueWork { task() } },
						sender = this,
					),
					type.codec.decode(payload.data)
				)
			}
		}

		val currentEvent = lastEvent
		if (currentEvent != null) {
			action(currentEvent)
		} else {
			synchronized(queuedClientHandlers) {
				queuedClientHandlers.add(action)
			}
		}
	}

	companion object {
		private var lastEvent: RegisterClientPayloadHandlersEvent? = null
		private val queuedClientHandlers = mutableListOf<(RegisterClientPayloadHandlersEvent) -> Unit>()

		internal fun registrationEvent(event: RegisterClientPayloadHandlersEvent) {
			lastEvent = event
			synchronized(queuedClientHandlers) {
				queuedClientHandlers.forEach { it(event) }
				queuedClientHandlers.clear()
			}
		}
	}
}
