package dev.pandasystems.pandalib.utils

enum class Environment {
	CLIENT,
	SERVER;

	val isClient get() = this == CLIENT
	val isServer get() = this == SERVER
	
	val opposite get() = if (this == CLIENT) SERVER else CLIENT
}