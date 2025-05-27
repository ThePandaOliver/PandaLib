
package dev.pandasystems.pandalib.event.events

import dev.architectury.event.Event
import dev.architectury.event.EventFactory
import dev.pandasystems.pandalib.networking.NetworkRegistry

interface NetworkingEvents {
	fun interface PacketPayloadRegistry {
		fun register(registry: NetworkRegistry)
	}

	companion object {
		@JvmField
		val PACKET_PAYLOAD_REGISTRY: Event<PacketPayloadRegistry> = EventFactory.createLoop<PacketPayloadRegistry>()
	}
}
