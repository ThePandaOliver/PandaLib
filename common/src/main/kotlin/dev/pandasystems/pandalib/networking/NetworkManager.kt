package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.core.PandaLibMain

interface NetworkManager : PacketSender, NetworkRegistrar

val networkManager: NetworkManager
	get() = PandaLibMain.instance.networkManager