package dev.pandasystems.pandalib.event

import java.lang.reflect.Proxy
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

interface Event<T> {
	val invoker: T

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
		override val invoker: T = createInvoker(listeners)

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
	{
		for (listener in listeners) {
			listener()
		}
	}
}

fun <A> event1() = event<(A) -> Unit> { listeners ->
	{ value ->
		for (listener in listeners) {
			listener(value)
		}
	}
}

fun <A, B> event2() = event<(A, B) -> Unit> { listeners ->
	{ first, second ->
		for (listener in listeners) {
			listener(first, second)
		}
	}
}

fun <A, B, C> event3() = event<(A, B, C) -> Unit> { listeners ->
	{ first, second, third ->
		for (listener in listeners) {
			listener(first, second, third)
		}
	}
}

fun <A, B, C, D> event4() = event<(A, B, C, D) -> Unit> { listeners ->
	{ first, second, third, fourth ->
		for (listener in listeners) {
			listener(first, second, third, fourth)
		}
	}
}

fun <A, B, C, D, E> event5() = event<(A, B, C, D, E) -> Unit> { listeners ->
	{ first, second, third, fourth, fifth ->
		for (listener in listeners) {
			listener(first, second, third, fourth, fifth)
		}
	}
}

fun eventCancelable0() = event<() -> Boolean> { listeners ->
	{
		for (listener in listeners) {
			if (!listener()) {
				return@event false
			}
		}

		return@event true
	}
}

fun <A> eventCancelable1() = event<(A) -> Boolean> { listeners ->
	{ value ->
		for (listener in listeners) {
			if (!listener(value)) {
				return@event false
			}
		}

		return@event true
	}
}

fun <A, B> eventCancelable2() = event<(A, B) -> Boolean> { listeners ->
	{ first, second ->
		for (listener in listeners) {
			if (!listener(first, second)) {
				return@event false
			}
		}

		return@event true
	}
}

fun <A, B, C> eventCancelable3() = event<(A, B, C) -> Boolean> { listeners ->
	{ first, second, third ->
		for (listener in listeners) {
			if (!listener(first, second, third)) {
				return@event false
			}
		}

		return@event true
	}
}

fun <A, B, C, D> eventCancelable4() = event<(A, B, C, D) -> Boolean> { listeners ->
	{ first, second, third, fourth ->
		for (listener in listeners) {
			if (!listener(first, second, third, fourth)) {
				return@event false
			}
		}

		return@event true
	}
}

fun <A, B, C, D, E> eventCancelable5() = event<(A, B, C, D, E) -> Boolean> { listeners ->
	{ first, second, third, fourth, fifth ->
		for (listener in listeners) {
			if (!listener(first, second, third, fourth, fifth)) {
				return@event false
			}
		}

		return@event true
	}
}