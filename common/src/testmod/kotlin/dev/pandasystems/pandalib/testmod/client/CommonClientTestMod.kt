package dev.pandasystems.pandalib.testmod

import dev.pandasystems.pandalib.testmod.networking.ClientNetworkTestManager

object CommonClientTestMod {
	fun initClient() {
		ClientNetworkTestManager.registerClientHandlers()
	}
}
