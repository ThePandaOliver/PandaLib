/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.event

import com.google.common.reflect.AbstractInvocationHandler
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.lang.reflect.Proxy

@Suppress("UNCHECKED_CAST")
fun <T : Function<R>, R> event(
	classType: Class<T>,
	combiner: (List<R>) -> R,
	shouldContinue: (R) -> Boolean = { true }
): Event<T> = Event { listeners ->
	Proxy.newProxyInstance(
		classType.classLoader ?: Thread.currentThread().contextClassLoader,
		arrayOf(classType),
		object : AbstractInvocationHandler() {
			override fun handleInvocation(proxy: Any, method: Method, args: Array<*>): Any? {
				val returnList = mutableListOf<R>()
				for (listener in listeners) {
					val returnValue = MethodHandles.lookup().unreflect(method)
						.bindTo(listener).invokeWithArguments(*args) as R
					
					returnList += returnValue
					if (!shouldContinue(returnValue)) {
						break
					}
				}
				return combiner(returnList)
			}
		}
	) as T
}

inline fun <reified T : Function<R>, R> event(
	noinline shouldContinue: (R) -> Boolean = { true },
	noinline combiner: (List<R>) -> R
): Event<T> = event(T::class.java, combiner, shouldContinue)

inline fun <reified T : Function<Unit>> event(): Event<T> = event { }

inline fun <reified T : Function<R>, R> eventLastResult(defaultValue: R): Event<T> = event { results -> results.lastOrNull() ?: defaultValue }

inline fun <reified T : Function<Boolean>> cancelableEvent(shouldCancelRemaining: Boolean = true): Event<T> =
	event({ if (shouldCancelRemaining) it else true }, { results -> results.all { it } })

class Event<T : Function<*>>(private val invokerFunction: (List<T>) -> T) {
	private var _invoker: T? = null
	private val listeners = mutableListOf<T>()

	val invoker: T
		get() {
			if (_invoker == null) {
				update()
			}
			return _invoker as T
		}
	
	fun subscribe(listener: T) {
		listeners += listener
		_invoker = null
	}
	
	fun unsubscribe(listener: T) {
		listeners -= listener
		_invoker = null
	}

	operator fun plusAssign(listener: T) = subscribe(listener)
	operator fun minusAssign(listener: T) = unsubscribe(listener)

	private fun update() {
		_invoker = if (listeners.size == 1) {
			listeners[0]
		} else {
			invokerFunction(listeners)
		}
	}
}