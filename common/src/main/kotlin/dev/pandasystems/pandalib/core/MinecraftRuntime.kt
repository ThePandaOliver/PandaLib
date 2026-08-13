package dev.pandasystems.pandalib.core

val minecraftRuntime: MinecraftRuntime get() = PandaLibMain.instance.minecraftRuntime

interface MinecraftRuntime {
	val type: MinecraftRuntimeType
	val environment: MinecraftRuntimeEnvironment
}

enum class MinecraftRuntimeType {
	FABRIC,
	NEO_FORGE
}

enum class MinecraftRuntimeEnvironment {
	CLIENT,
	SERVER,
}