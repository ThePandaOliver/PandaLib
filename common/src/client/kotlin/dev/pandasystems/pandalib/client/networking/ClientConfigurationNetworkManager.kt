package dev.pandasystems.pandalib.client.networking

import dev.pandasystems.pandalib.core.utils.loadService

interface ClientConfigurationNetworkManager : ClientConfigurationPacketSender, ClientConfigurationNetworkRegistrar {
    companion object : ClientConfigurationNetworkManager by loadService()
}
