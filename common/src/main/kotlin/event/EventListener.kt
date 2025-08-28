/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.event

import dev.pandasystems.pandalib.logger
import java.util.function.Consumer

object EventListener {
	private val eventListenerLists = mutableMapOf<Class<*>, MutableList<Consumer<Event>>>()

	inline fun <reified E : Event> add(listener: Consumer<E>, priority: Int = 0) {
		add(E::class.java, listener, priority)
	}

	@JvmStatic
	@JvmOverloads
	fun <E : Event> add(eventClass: Class<E>, listener: Consumer<E>, priority: Int = 0) {
		val listeners = eventListenerLists.getOrDefault(eventClass, mutableListOf())
		@Suppress("UNCHECKED_CAST")
		listeners.add(listener as Consumer<Event>)
		eventListenerLists[eventClass] = listeners
		logger.debug("Added event listener for ${eventClass.simpleName}. Total listeners: ${listeners.size}")
	}

	inline fun <reified E : Event> remove(listener: Consumer<E>) {
		remove(E::class.java, listener)
	}

	@JvmStatic
	fun <E : Event> remove(eventClass: Class<E>, listener: Consumer<E>) {
		val listeners = eventListenerLists[eventClass]
		listeners?.let {
			@Suppress("UNCHECKED_CAST")
			it.remove(listener as Consumer<Event>)
			if (it.isEmpty()) {
				eventListenerLists.remove(eventClass)
			}
		}
	}

	@JvmStatic
	fun invoke(event: Event) {
		eventListenerLists[event::class.java]?.forEach {
			it.accept(event)
			logger.debug("Invoked event listener for ${event::class.java.simpleName} with event: $event")
		}
	}
}