package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.core.handles.Identifier
import java.util.function.Supplier

interface DeferredRegistry<T : Any> {
	val hasRegistered: Boolean

	fun register(id: Identifier, value: (id: Identifier) -> T): DeferredHolder<T>

	fun get(id: Identifier): T = getHolder(id).get()

	fun getHolder(id: Identifier): DeferredHolder<T> = holders[id] ?: throw IllegalStateException("$id has not been registered")

	val holders: Map<Identifier, DeferredHolder<T>>
}