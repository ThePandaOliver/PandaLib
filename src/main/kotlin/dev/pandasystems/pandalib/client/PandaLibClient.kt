

package dev.pandasystems.pandalib.client

import dev.pandasystems.pandalib.core.client.event.EventHandlerClient
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
object PandaLibClient {
	@JvmStatic
	fun init() {
		EventHandlerClient.init()
	}
}