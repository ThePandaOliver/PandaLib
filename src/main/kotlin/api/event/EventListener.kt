/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("EventListener")

package dev.pandasystems.pandalib.api.event

import dev.pandasystems.pandalib.core.logger
import java.util.function.Consumer

private val eventListenerLists = mutableMapOf<Class<*>, MutableList<Consumer<Event>>>()

inline fun <reified E : Event> addEventListener(listener: Consumer<E>, priority: Int = 0) {
	addEventListener(E::class.java, listener, priority)
}

@JvmName("addListener")
@JvmOverloads
fun <E : Event> addEventListener(eventClass: Class<E>, listener: Consumer<E>, priority: Int = 0) {
	val listeners = eventListenerLists.getOrDefault(eventClass, mutableListOf())
	@Suppress("UNCHECKED_CAST")
	listeners.add(listener as Consumer<Event>)
	eventListenerLists[eventClass] = listeners
	logger.debug("Added event listener for ${eventClass.simpleName}. Total listeners: ${listeners.size}")
}

inline fun <reified E : Event> removeEventListener(listener: Consumer<E>) {
	removeEventListener(E::class.java, listener)
}

@JvmName("removeListener")
fun <E : Event> removeEventListener(eventClass: Class<E>, listener: Consumer<E>) {
	val listeners = eventListenerLists[eventClass]
	listeners?.let {
		@Suppress("UNCHECKED_CAST")
		it.remove(listener as Consumer<Event>)
		if (it.isEmpty()) {
			eventListenerLists.remove(eventClass)
		}
	}
}

fun invokeEvent(event: Event) {
	eventListenerLists[event::class.java]?.forEach {
		it.accept(event)
		logger.debug("Invoked event listener for ${event::class.java.simpleName} with event: $event")
	}
}