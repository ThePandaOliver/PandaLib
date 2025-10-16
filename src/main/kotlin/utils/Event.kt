/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.utils

import com.google.common.reflect.AbstractInvocationHandler
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.lang.reflect.Proxy

@JvmOverloads
@Suppress("UNCHECKED_CAST")
fun <T : Function<R>, R> event(
	classType: Class<T>,
	combiner: (List<R>) -> R,
	shouldContinue: (R) -> Boolean = { true }
): Event<T> {
	fun <T, R> invokeMethod(listener: T, method: Method, args: Array<*>): R {
		return MethodHandles.lookup().unreflect(method)
			.bindTo(listener).invokeWithArguments(*args) as R
	}

	return Event { listeners ->
		Proxy.newProxyInstance(classType.classLoader ?: Thread.currentThread().contextClassLoader, arrayOf(classType), object : AbstractInvocationHandler() {
			override fun handleInvocation(proxy: Any, method: Method, args: Array<*>): Any? {
				val returnList = mutableListOf<R>()
				for (listener in listeners) {
					val returnValue = invokeMethod<T, R>(listener, method, args)
					returnList += returnValue
					if (!shouldContinue(returnValue)) {
						break
					}
				}
				return combiner(returnList)
			}
		}) as T
	}
}

inline fun <reified T : Function<R>, R> event(
	noinline shouldContinue: (R) -> Boolean = { true },
	noinline combiner: (List<R>) -> R
): Event<T> = event(T::class.java, combiner, shouldContinue)

inline fun <reified T : Function<Unit>> event(): Event<T> = event { }

inline fun <reified T : Function<R>, R> eventLastResult(defaultValue: R): Event<T> = event { results -> results.lastOrNull() ?: defaultValue }

inline fun <reified T : Function<Boolean>> cancelableEvent(shouldCancelRemaining: Boolean = true): Event<T> =
	event({ if (shouldCancelRemaining) it else true }, { results -> results.all { it } })

class Event<T : Function<*>>(private val function: (MutableList<T>) -> T) {
	private var _invoker: T? = null
	private val listeners = mutableListOf<Pair<Int, T>>()

	val invoker: T
		get() {
			if (_invoker == null) {
				update()
			}
			return _invoker as T
		}

	fun register(priority: Int = 0, handler: T) {
		listeners.add(priority to handler)
		listeners.sortByDescending { it.first }
		_invoker = null
	}

	fun unregister(handler: T) {
		listeners.removeAll { it.second == handler }
		_invoker = null
	}

	operator fun plusAssign(handler: T) = register(handler = handler)
	operator fun plusAssign(pair: Pair<Int, T>) = register(priority = pair.first, handler = pair.second)
	operator fun minusAssign(handler: T) = unregister(handler = handler)

	private fun update() {
		_invoker = if (listeners.size == 1) {
			listeners[0].second
		} else {
			function(listeners.map { it.second }.toMutableList())
		}
	}
}