package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.core.handles.Identifier

interface DeferredObject<T> {
	val key: Identifier

	val isBound: Boolean

	fun get(): T

	fun onBind(listener: (T) -> Unit)

	operator fun invoke(): T = get()
}
