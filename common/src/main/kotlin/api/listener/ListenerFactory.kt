/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.listener

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

	inline fun <reified E : Any, reified T : Listener<E>> create(): T {
		return create(E::class.java) as T
	}

	@Suppress("UNCHECKED_CAST")
	fun <T : Any> create(clazz: Class<T>): Listener<T> {
		return of { listeners ->
			Proxy.newProxyInstance(ListenerFactory::class.java.classLoader, arrayOf(clazz), object : AbstractInvocationHandler() {
				override fun handleInvocation(proxy: Any, method: Method, args: Array<*>): Any? {
					listeners.forEach { listener -> invokeMethod(listener, method, args)}
					return null
				}
			}) as T
		}
	}

	private class ListenerImpl<T : Any>(
		private val function: (MutableList<T>) -> T
	) : Listener<T> {
		private val listeners = mutableListOf<T>()
		private var invoker: T? = null

		override fun register(listener: T) {
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

		override fun invoker(): T {
			if (invoker == null) {
				update()
			}
			return requireNotNull(invoker) { "No invoker found" }
		}

		fun update() {
			invoker = if (listeners.size == 1) {
				listeners[0]
			} else {
				function(listeners)
			}
		}
	}
}