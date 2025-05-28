package dev.pandasystems.pandalib.core

import dev.pandasystems.pandalib.impl.config.ConfigRegistry
import net.minecraft.resources.ResourceLocation

object PandaLib {
	const val MOD_ID = "pandalib"
	
	fun init() {
		ConfigRegistry.register(PandaLibConfig)
	}

	fun resourceLocation(path: String): ResourceLocation {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
	}
}