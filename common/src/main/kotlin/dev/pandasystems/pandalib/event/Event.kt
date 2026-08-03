package dev.pandasystems.pandalib.event

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

interface Event<T> {
	val invoke: T

	fun subscribe(listener: T): Subscription
}

fun interface Subscription {
	fun unsubscribe()
}

/**
 * Creates an event from a type-safe invoker implementation.
 *
 * The invoker is created once and invokes listeners directly, without
 * reflection or dynamic proxies.
 */
fun <T> event(
	createInvoker: (listeners: List<T>) -> T,
): Event<T> {
	val listeners = CopyOnWriteArrayList<T>()

	return object : Event<T> {
		override val invoke: T = createInvoker(listeners)

		override fun subscribe(listener: T): Subscription {
			listeners += listener

			val subscribed = AtomicBoolean(true)
			return Subscription {
				if (subscribed.compareAndSet(true, false)) {
					listeners -= listener
				}
			}
		}
	}
}

fun event0() = event { listeners ->
	{ listeners.forEach { it() } }
}

fun <A> event1() = event<(A) -> Unit> { listeners ->
	{ a -> listeners.forEach { it(a) } }
}

fun <A, B> event2() = event<(A, B) -> Unit> { listeners ->
	{ a, b -> listeners.forEach { it(a, b) } }
}

fun <A, B, C> event3() = event<(A, B, C) -> Unit> { listeners ->
	{ a, b, c -> listeners.forEach { it(a, b, c) } }
}

fun <A, B, C, D> event4() = event<(A, B, C, D) -> Unit> { listeners ->
	{ a, b, c, d -> listeners.forEach { it(a, b, c, d) } }
}

fun <A, B, C, D, E> event5() = event<(A, B, C, D, E) -> Unit> { listeners ->
	{ a, b, c, d, e -> listeners.forEach { it(a, b, c, d, e) } }
}

fun eventCancelable0() = event { listeners ->
	{ listeners.map { it() }.all { it } }
}

fun <A> eventCancelable1() = event<(A) -> Boolean> { listeners ->
	{ a -> listeners.map { it(a) }.all { it } }
}

fun <A, B> eventCancelable2() = event<(A, B) -> Boolean> { listeners ->
	{ a, b -> listeners.map { it(a, b) }.all { it } }
}

fun <A, B, C> eventCancelable3() = event<(A, B, C) -> Boolean> { listeners ->
	{ a, b, c -> listeners.map { it(a, b, c) }.all { it } }
}

fun <A, B, C, D> eventCancelable4() = event<(A, B, C, D) -> Boolean> { listeners ->
	{ a, b, c, d -> listeners.map { it(a, b, c, d) }.all { it } }
}

fun <A, B, C, D, E> eventCancelable5() = event<(A, B, C, D, E) -> Boolean> { listeners ->
	{ a, b, c, d, e -> listeners.map { it(a, b, c, d, e) }.all { it } }
}