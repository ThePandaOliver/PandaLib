package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.core.handles.Identifier
import net.minecraft.resources.ResourceKey
import java.util.function.Supplier

interface DeferredHolder<T : Any> : Supplier<T> {
	val hasRegistered: Boolean
	val key: ResourceKey<T>
	val value: T?
	override fun get(): T = value ?: throw IllegalStateException("${key.identifier()} has not been registered")
}