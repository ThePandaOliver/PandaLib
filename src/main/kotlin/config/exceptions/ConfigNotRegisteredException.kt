package dev.pandasystems.pandalib.config.exceptions

import net.minecraft.resources.ResourceLocation

class ConfigNotRegisteredException(resourceLocation: ResourceLocation) : RuntimeException() {
	override val message: String = "Config $resourceLocation is not registered"
}