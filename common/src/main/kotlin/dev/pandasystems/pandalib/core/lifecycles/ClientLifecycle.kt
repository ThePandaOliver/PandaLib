package dev.pandasystems.pandalib.core.lifecycles

import net.minecraft.client.Minecraft as MinecraftClient

object ClientLifecycle {
	val clientInstance: MinecraftClient get() = MinecraftClient.getInstance()
}