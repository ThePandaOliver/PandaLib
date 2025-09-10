/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.listener

import com.google.common.reflect.AbstractInvocationHandler
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.lang.reflect.Proxy


object ListenerFactory {
	fun <T : Any> of(function: (MutableList<T>) -> T): Listener<T> {
		return ListenerImpl(function)
	}

	@Suppress("UNCHECKED_CAST")
	private fun <T, R> invokeMethod(listener: T, method: Method, args: Array<*>): R {
		return MethodHandles.lookup().unreflect(method)
			.bindTo(listener).invokeWithArguments(*args) as R
	}

	inline fun <reified T : Function<Unit>> create(): Listener<T> {
		return create(T::class.java)
	}

	@Suppress("UNCHECKED_CAST")
	fun <T : Function<Unit>> create(clazz: Class<T>): Listener<T> {
		return of { listeners ->
			Proxy.newProxyInstance(ListenerFactory::class.java.classLoader, arrayOf(clazz), object : AbstractInvocationHandler() {
				override fun handleInvocation(proxy: Any, method: Method, args: Array<*>): Any? {
					listeners.forEach { listener -> invokeMethod(listener, method, args)}
					return null
				}
			}) as T
		}
	}

	inline fun <reified T : Function<Boolean>> createCancellable(): Listener<T> {
		return createCancellable(T::class.java)
	}

	@Suppress("UNCHECKED_CAST")
	fun <T : Function<Boolean>> createCancellable(clazz: Class<T>): Listener<T> {
		return of { listeners ->
			Proxy.newProxyInstance(ListenerFactory::class.java.classLoader, arrayOf(clazz), object : AbstractInvocationHandler() {
				override fun handleInvocation(proxy: Any, method: Method, args: Array<*>): Any? {
					listeners.forEach { listener ->
						val result = invokeMethod<T, Boolean>(listener, method, args)
						if (!result) return false
					}
					return true
				}
			}) as T
		}
	}

	class ListenerImpl<T : Any>(private val function: (MutableList<T>) -> T) : Listener<T> {
		private var invoker: T? = null
		private val listeners = mutableListOf<T>()

		override fun invoker(): T {
			if (invoker == null) {
				update()
			}
			return invoker as T
		}

		override fun register(priority: Int, listener: T) {
			listeners.add(listener)
			invoker = null
		}

		override fun unregister(listener: T) {
			listeners.remove(listener)
			invoker = null
		}

		override fun clear() {
			listeners.clear()
			invoker = null
		}

		fun update() {
			if (listeners.size == 1) {
				invoker = listeners[0]
			} else {
				invoker = function(listeners)
			}
		}
	}

//	private class ListenerImpl<T : Any>(
//		private val function: (MutableList<T>) -> T
//	) : Listener<T> {
//		private data class Entry<T>(val priority: Int, val order: Long, val listener: T)
//
//		private val entries = mutableListOf<Entry<T>>()
//		private var sequence: Long = 0L
//
//		private var orderedListeners: MutableList<T> = mutableListOf()
//		private var invoker: T? = null
//
//		override fun register(priority: Int, listener: T) {
//			val newEntry = Entry(priority, sequence++, listener)
//
//			entries.forEachIndexed { index, entry ->
//				if (newEntry.priority > entry.priority) {
//					entries.add(index, newEntry)
//					return@forEachIndexed
//				}
//			}
//
//			invoker = null
//		}
//
//		override fun unregister(listener: T) {
//			entries.removeAll { it.listener == listener }
//			invoker = null
//		}
//
//		override fun clear() {
//			entries.clear()
//			invoker = null
//		}
//
//		override fun invoker(): T {
//			if (invoker == null) {
//				update()
//			}
//			return requireNotNull(invoker) { "No invoker found" }
//		}
//
//		fun update() {
//			// entries are already kept ordered by priority desc and stable among equals
//			orderedListeners = entries.mapTo(mutableListOf()) { it.listener }
//
//			invoker = if (orderedListeners.size == 1) {
//				orderedListeners[0]
//			} else {
//				function(orderedListeners)
//			}
//		}
//	}
}