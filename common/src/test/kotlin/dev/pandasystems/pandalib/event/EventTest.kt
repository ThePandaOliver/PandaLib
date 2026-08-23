package dev.pandasystems.pandalib.event

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EventTest {

	private val testCustomEvent by event<String, Unit> { listeners, context ->
		listeners.forEach { it(context) }
	}

	private val testCustomReturnEvent by event<Int, String> { listeners, context ->
		listeners.joinToString(",") { it(context) }
	}

	private val testStandardEvent by event<String>()
	private val testCancelableEvent by eventCancelable<String>()

	@Test
	fun `direct construction sets event name`() {
		val custom = event<String, Unit>("customDirect") { listeners, context ->
			listeners.forEach { it(context) }
		}
		assertEquals("customDirect", custom.name)

		val standard = event<String>("standardDirect")
		assertEquals("standardDirect", standard.name)

		val cancelable = eventCancelable<String>("cancelableDirect")
		assertEquals("cancelableDirect", cancelable.name)
	}

	@Test
	fun `delegated construction sets event name to property name`() {
		assertEquals("testCustomEvent", testCustomEvent.name)
		assertEquals("testCustomReturnEvent", testCustomReturnEvent.name)
		assertEquals("testStandardEvent", testStandardEvent.name)
		assertEquals("testCancelableEvent", testCancelableEvent.name)
	}

	@Test
	fun `subscribing and invoking works for standard events`() {
		var received = ""
		val sub = testStandardEvent.subscribe { context ->
			received = context
		}

		testStandardEvent("hello")
		assertEquals("hello", received)

		testStandardEvent.invoke("world")
		assertEquals("world", received)

		sub.unsubscribe()
		testStandardEvent("after unsubscribe")
		assertEquals("world", received)
	}

	@Test
	fun `subscribing and invoking works for cancelable events`() {
		// Default with no listeners is true
		assertTrue(testCancelableEvent("check"))

		val sub1 = testCancelableEvent.subscribe { context ->
			context != "cancel"
		}

		assertTrue(testCancelableEvent("ok"))
		assertFalse(testCancelableEvent("cancel"))

		val sub2 = testCancelableEvent.subscribe { _ ->
			false
		}
		assertFalse(testCancelableEvent("ok"))

		sub2.unsubscribe()
		assertTrue(testCancelableEvent("ok"))

		sub1.unsubscribe()
		assertTrue(testCancelableEvent("cancel"))
	}

	@Test
	fun `subscribing and invoking works for custom return events`() {
		testCustomReturnEvent.subscribe { "a:$it" }
		testCustomReturnEvent.subscribe { "b:$it" }

		val result = testCustomReturnEvent(42)
		assertEquals("a:42,b:42", result)
	}
}
