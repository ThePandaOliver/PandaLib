package dev.pandasystems.pandalib.neoforge.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.networking.*
import net.neoforged.neoforge.client.network.ClientPacketDistributor
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent

@AutoService(ClientPlayNetworkManager::class)
class NeoForgeClientPlayNetworkManager : ClientPlayNetworkManager {
	override fun <T> sendToServer(type: PacketType<T>, value: T) {
		NeoForgePlayNetworkManager.checkRegistered(type, PacketDirection.CLIENT_TO_SERVER)
		ClientPacketDistributor.sendToServer(NeoForgePlayNetworkManager.payload(type, value))
	}

	override fun <T> register(
		type: PacketType<T>,
		handler: ClientPlayPacketHandler<T>,
	) {
		NeoForgePlayNetworkManager.checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)

		val action: (RegisterClientPayloadHandlersEvent) -> Unit = { event ->
			val payloadType = NeoForgePacketPayload.type(type.id)
			event.register(payloadType) { payload, context ->
				handler.handle(
					ClientPlayPacketContextImpl(
						player = context.player(),
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
