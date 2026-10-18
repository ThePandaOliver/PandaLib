package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.core.handles.Identifier
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface DeferredObject<out T> : ReadOnlyProperty<Any?, T> {
	val key: Identifier
	val isBound: Boolean

	fun get(): T
	fun onBind(listener: (T) -> Unit)

	operator fun invoke(): T = get()
	override fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
}

class CompletableDeferredObject<T>(override val key: Identifier) : DeferredObject<T> {
	private var value: T? = null
	private val listeners = CopyOnWriteArrayList<(T) -> Unit>()

	@Volatile
	override var isBound: Boolean = false
		private set

	override fun get(): T {
		check(isBound) { "DeferredObject '$key' has not been bound yet." }
		@Suppress("UNCHECKED_CAST")
		return value as T
	}

	override fun onBind(listener: (T) -> Unit) {
		if (isBound) {
			listener(get())
		} else {
			listeners += listener
		}
	}

	internal fun bind(boundValue: T) {
		check(!isBound) { "DeferredObject '$key' is already bound." }
		value = boundValue
		isBound = true
		listeners.forEach { it(boundValue) }
		listeners.clear()
	}
}