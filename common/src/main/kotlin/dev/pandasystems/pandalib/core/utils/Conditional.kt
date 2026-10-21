package dev.pandasystems.pandalib.core.utils

import dev.pandasystems.pandalib.core.MinecraftRuntime
import dev.pandasystems.pandalib.core.RuntimeEnvironment
import dev.pandasystems.pandalib.core.lifecycles.ServerLifecycle

/**
 * Makes the given block of code run only on the server.
 */
inline fun server(block: () -> Unit) {
	if (MinecraftRuntime.environment == RuntimeEnvironment.SERVER) block()
}

/**
 * Makes the given block of code run only on the client.
 */
inline fun client(block: () -> Unit) {
	if (MinecraftRuntime.environment == RuntimeEnvironment.CLIENT) block()
}

/**
 * Makes the given block of code run only on the host.
 */
inline fun host(block: () -> Unit) {
	if (ServerLifecycle.instance != null) block()
}