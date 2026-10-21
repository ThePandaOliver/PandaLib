package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.core.utils.loadService

interface PlayPlayNetworkManager : PlayPacketSender, PlayNetworkRegistrar {
	companion object : PlayPlayNetworkManager by loadService()
}