package dev.pandasystems.pandalib.config.exceptions

import net.minecraft.resources.Identifier

class ConfigNotRegisteredException(resourceLocation: Identifier) : RuntimeException() {
	override val message: String = "Config $resourceLocation is not registered"
}