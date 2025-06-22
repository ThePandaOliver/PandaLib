package dev.pandasystems.pandalib.core

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.impl.config.ConfigRegistry
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger


object PandaLib {
	const val MOD_ID = "pandalib"
	
	val config = ConfigRegistry.register<PandaLibConfig>()
	
	init {
		logger.debug("PandaLib Core is initializing...")

		logger.debug("PandaLib Core initialized successfully.")
	}
	
	@JvmStatic
	fun resourceLocation(path: String): ResourceLocation {
		return dev.pandasystems.pandalib.utils.extensions.resourceLocation(MOD_ID, path)
	}
}

val logger: Logger = LogUtils.getLogger()