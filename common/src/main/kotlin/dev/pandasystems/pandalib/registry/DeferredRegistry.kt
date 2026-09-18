package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.core.handles.Identifier

interface DeferredRegistry<T> {
	fun register(key: Identifier, provider: (key: Identifier) -> T): DeferredObject<T>

	companion object {
		fun <T> create(): DeferredRegistry<T> = object : DeferredRegistry<T> {
			override fun register(key: Identifier, provider: (key: Identifier) -> T): DeferredObject<T> {
				throw UnsupportedOperationException("DeferredRegistry.create() does not support registration")
			}
		}
	}
}