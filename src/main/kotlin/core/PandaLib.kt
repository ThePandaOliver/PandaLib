package dev.pandasystems.pandalib.core

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.impl.config.ConfigRegistry
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger

object PandaLib {
	const val MOD_ID = "pandalib"
	val logger: Logger = LogUtils.getLogger()
	
	init {
		logger.debug("PandaLib Core is initializing...")
		ConfigRegistry.register(PandaLibConfig)
		logger.debug("PandaLib Core initialized successfully.")
	}

	@JvmStatic
	fun resourceLocation(path: String): ResourceLocation {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
	}
}