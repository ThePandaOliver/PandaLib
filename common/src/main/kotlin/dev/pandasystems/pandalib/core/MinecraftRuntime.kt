package dev.pandasystems.pandalib.core

interface IMinecraftRuntime {
	val type: RuntimeType
	val environment: RuntimeEnvironment
}

object MinecraftRuntime : IMinecraftRuntime by PandaLibMain.instance.minecraftRuntime

enum class RuntimeType {
	FABRIC,
	NEO_FORGE
}

enum class RuntimeEnvironment {
	CLIENT,
	SERVER,
}