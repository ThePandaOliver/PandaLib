package dev.pandasystems.pandalib.core

import dev.pandasystems.pandalib.impl.config.Configuration

@Configuration(PandaLib.MOD_ID, "pandalib")
data object PandaLibConfig {
	var clientDebug: Boolean = false
	var serverDebug: Boolean = false
}
