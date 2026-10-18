package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.core.handles.Identifier
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.text.clear

interface DeferredRegistry<T> {
	fun <I : T> register(key: Identifier, provider: (key: Identifier) -> I): DeferredObject<I>

	companion object {
		fun <T> create(): Standalone<T> = Standalone()

		fun <T> delegated(
			delegateRegistrar: (key: Identifier, supplier: () -> T) -> Unit
		): DeferredRegistry<T> = object : DeferredRegistry<T> {
			override fun <I : T> register(key: Identifier, provider: (key: Identifier) -> I): DeferredObject<I> {
				val obj = CompletableDeferredObject<I>(key)
				delegateRegistrar(key) {
					provider(key).also { obj.bind(it) }
				}
				return obj
			}
		}

		class Standalone<T> : DeferredRegistry<T> {
			private val entries = mutableListOf<Pair<Identifier, () -> T>>()
			private var activeDelegate: ((Identifier, () -> T) -> Unit)? = null

			override fun <I : T> register(key: Identifier, provider: (key: Identifier) -> I): DeferredObject<I> {
				val obj = CompletableDeferredObject<I>(key)
				val wrappedSupplier: () -> I = {
					provider(key).also { obj.bind(it) }
				}

				val delegate = activeDelegate
				if (delegate != null) {
					delegate(key, wrappedSupplier)
				} else {
					entries += key to wrappedSupplier
				}

				return obj
			}

			fun registerTo(registrar: (key: Identifier, value: T) -> Unit) {
				delegateTo { key, supplier ->
					registrar(key, supplier())
				}
			}

			fun delegateTo(delegateRegistrar: (key: Identifier, supplier: () -> T) -> Unit) {
				check(activeDelegate == null) { "DeferredRegistry has already been bound or delegated." }
				activeDelegate = delegateRegistrar
				entries.forEach { (key, supplier) ->
					delegateRegistrar(key, supplier)
				}
				entries.clear()
			}
		}
	}
}