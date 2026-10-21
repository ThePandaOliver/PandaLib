package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.core.utils.loadService

interface ClientPlayNetworkManager : ClientPlayPacketSender, ClientPlayNetworkRegistrar {
    companion object : ClientPlayNetworkManager by loadService()
}
