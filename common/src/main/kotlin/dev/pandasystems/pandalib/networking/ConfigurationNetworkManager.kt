package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.core.utils.loadService

interface ConfigurationNetworkManager : ConfigurationPacketSender, ConfigurationNetworkRegistrar {
    companion object : ConfigurationNetworkManager by loadService()
}
