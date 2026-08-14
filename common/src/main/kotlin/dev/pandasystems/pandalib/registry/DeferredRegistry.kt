package dev.pandasystems.pandalib.registry

/**
 * A Minecraft-independent registry for values that must be declared early, but can only be
 * really registered later (e.g. during a mod loader's registration event).
 *
 * Typical usage:
 * ```
 * val ITEMS = DeferredRegistry<Identifier, Item>()
 *
 * // Early, e.g. during mod construction:
 * val sword: RegistryEntry<Item> = ITEMS.register(identifier("mymod", "sword")) { Item(...) }
 *
 * // Later, e.g. during the loader's registration event:
 * ITEMS.registerAll { id, factory -> platformRegistry.register(id, factory()) }
 *
 * // From that point onward:
 * val item = sword.get()
 * ```
 *
 * Every entry returned by [register] is a stable [RegistryEntry] handle that can be stored,
 * passed around, and captured in closures immediately. The real value can only be retrieved
 * through [RegistryEntry.get] once [registerAll] has bound it.
 *
 * Instances of this class are thread-safe.
 */
class DeferredRegistry<K : Any, T> {
	private val entries = LinkedHashMap<K, Entry>()
	private val lock = Any()

	@Volatile
	var isFinalized: Boolean = false
		private set

	/**
	 * The keys of all entries registered so far, in registration order.
	 */
	val keys: Set<K>
		get() = synchronized(lock) { LinkedHashSet(entries.keys) }

	/**
	 * The amount of entries registered so far.
	 */
	val size: Int
		get() = synchronized(lock) { entries.size }

	/**
	 * Declares a new deferred entry.
	 *
	 * [factory] is not invoked here; it is only invoked once the real registration happens
	 * through [registerAll]. The returned [RegistryEntry] can be used right away, but calling
	 * [RegistryEntry.get] before that point will throw.
	 *
	 * @throws IllegalArgumentException if [key] is already registered.
	 * @throws IllegalStateException if this registry has already been finalized via [registerAll].
	 */
	fun register(key: K, factory: () -> T): RegistryEntry<T> = synchronized(lock) {
		check(!isFinalized) {
			"Cannot register '$key': this registry has already been finalized."
		}
		require(key !in entries) {
			"A value is already registered with key '$key'."
		}

		val entry = Entry(key, factory)
		entries[key] = entry
		entry
	}

	/**
	 * Performs the real registration for every entry declared so far, using [register] as the
	 * actual registration action. [register] receives each entry's key and factory, and must
	 * return the real, registered value.
	 *
	 * After this call, this registry is finalized: every current entry becomes gettable through
	 * its [RegistryEntry], and no further entries may be added.
	 *
	 * @throws IllegalStateException if this registry has already been finalized.
	 */
	fun registerAll(register: (key: K, factory: () -> T) -> T) {
		val snapshot = synchronized(lock) {
			check(!isFinalized) { "This registry has already been finalized." }
			isFinalized = true
			entries.values.toList()
		}

		snapshot.forEach { entry -> entry.bind(register(entry.key, entry.factory)) }
	}

	/**
	 * Returns the entry registered under [key], or `null` if no such entry exists.
	 */
	fun getEntry(key: K): RegistryEntry<T>? = synchronized(lock) { entries[key] }

	/**
	 * Returns `true` if a value is registered under [key], regardless of whether it has been
	 * bound to its real value yet.
	 */
	fun containsKey(key: K): Boolean = synchronized(lock) { key in entries }

	/**
	 * Returns a snapshot of all entries registered so far, in registration order.
	 */
	fun entries(): List<RegistryEntry<T>> = synchronized(lock) { entries.values.toList() }

	/**
	 * Runs [action] for every entry registered so far, in registration order.
	 */
	fun forEach(action: (key: K, entry: RegistryEntry<T>) -> Unit) {
		val snapshot = synchronized(lock) { entries.toList() }
		snapshot.forEach { (key, entry) -> action(key, entry) }
	}

	private inner class Entry(
		override val key: K,
		val factory: () -> T,
	) : RegistryEntry<T> {
		private var value: Any? = UNBOUND
		private var bindListeners: MutableList<(T) -> Unit>? = null

		override val isBound: Boolean
			get() = synchronized(lock) { value !== UNBOUND }

		fun bind(bound: T) {
			val listeners = synchronized(lock) {
				check(value === UNBOUND) { "Entry for key '$key' has already been registered." }
				value = bound
				bindListeners?.toList().also { bindListeners = null }
			}
			listeners?.forEach { it(bound) }
		}

		/**
		 * Registers a [listener] to be invoked once this entry is bound to its real value.
		 * If the entry is already bound, the listener is invoked immediately.
		 */
		override fun onBind(listener: (T) -> Unit) {
			val boundValue = synchronized(lock) {
				if (value !== UNBOUND) return@synchronized value
				(bindListeners ?: mutableListOf<(T) -> Unit>().also { bindListeners = it }) += listener
				UNBOUND
			}
			@Suppress("UNCHECKED_CAST")
			if (boundValue !== UNBOUND) listener(boundValue as T)
		}

		@Suppress("UNCHECKED_CAST")
		override fun get(): T {
			val current = synchronized(lock) { value }
			check(current !== UNBOUND) {
				"Value for key '$key' has not been registered yet. " +
					"It can only be retrieved after the deferred registry has been finalized via registerAll()."
			}
			return current as T
		}

		override fun toString(): String =
			if (isBound) "RegistryEntry[$key -> ${get()}]" else "RegistryEntry[$key <unbound>]"
	}

	private companion object {
		val UNBOUND = Any()
	}
}
