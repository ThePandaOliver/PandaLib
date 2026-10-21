package dev.pandasystems.pandalib.core.lifecycles

import net.minecraft.client.Minecraft as MinecraftClient

object ClientLifecycle {
	val instance: MinecraftClient get() = MinecraftClient.getInstance()

	internal fun initialize() = Unit
}
