package dev.pandasystems.pandalib.registry

/**
 * A handle to a value that is registered early but only becomes available once the
 * corresponding [DeferredRegistry] has actually performed the real registration.
 *
 * Instances are handed out immediately by [DeferredRegistry.register], long before the
 * real value exists. They are safe to store in `val` fields, pass around, and capture in
 * closures right away; only calling [get] before the entry is bound will fail.
 */
interface RegistryEntry<T> {
	/**
	 * The key this entry was registered with.
	 */
	val key: Any?

	/**
	 * Whether the real registration has already happened and [get] can be called safely.
	 */
	val isBound: Boolean

	/**
	 * Returns the registered value.
	 *
	 * @throws IllegalStateException if the value has not been registered yet.
	 */
	fun get(): T

	/**
	 * Registers a [listener] to be invoked once this entry is bound to its real value.
	 *
	 * If the entry is already bound, the listener is invoked immediately with the current value.
	 */
	fun onBind(listener: (T) -> Unit)

	/**
	 * Shorthand for [get], allowing entries to be used like `val value = entry()`.
	 */
	operator fun invoke(): T = get()
}
