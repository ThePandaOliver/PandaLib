package dev.pandasystems.pandalib.utils

import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer

val gameEnvironment = loadFirstService<GameEnvironmentPlatform>()

interface GameEnvironmentPlatform {
	val isDevelopment: Boolean
	val isProduction: Boolean

	val environment: Environment
	val isClient
		get() = environment.isClient
	val isDedicatedServer
		get() = environment.isDedicatedServer

	val isHost: Boolean
		get() = server != null

	val client: Minecraft get() = Minecraft.getInstance()
	val server: MinecraftServer?
}