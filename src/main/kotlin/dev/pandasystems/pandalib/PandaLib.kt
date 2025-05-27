

package dev.pandasystems.pandalib

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.core.event.EventHandler
import dev.pandasystems.pandalib.core.network.ConfigNetworking
import dev.pandasystems.pandalib.event.events.NetworkingEvents
import net.minecraft.resources.ResourceLocation
import org.slf4j.event.Level

object PandaLib {
	const val MOD_ID = "pandalib"
	
	fun init() {
		LogUtils.configureRootLoggingLevel(Level.DEBUG)
		NetworkingEvents.PACKET_PAYLOAD_REGISTRY.register(ConfigNetworking::registerPackets)
		EventHandler.init()
	}
	
	fun resourceLocation(path: String): ResourceLocation {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
	}
}
