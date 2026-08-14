package dev.pandasystems.pandalib.registry

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeferredRegistryTest {

	@Test
	fun `entry is unbound and unusable before finalization`() {
		val registry = DeferredRegistry<String, String>()

		val entry = registry.register("sword") { "Sword" }

		assertFalse(entry.isBound)
		assertFailsWith<IllegalStateException> { entry.get() }
	}

	@Test
	fun `entry becomes gettable after registerAll runs`() {
		val registry = DeferredRegistry<String, String>()

		val entry = registry.register("sword") { "Sword" }
		registry.registerAll { _, factory -> factory() }

		assertTrue(entry.isBound)
		assertEquals("Sword", entry.get())
		assertEquals("Sword", entry())
	}

	@Test
	fun `registerAll receives the key and can transform the factory result`() {
		val registry = DeferredRegistry<String, String>()

		val greeting = registry.register("hello") { "world" }
		registry.registerAll { key, factory -> "$key ${factory()}" }

		assertEquals("hello world", greeting.get())
	}

	@Test
	fun `registering the same key twice fails`() {
		val registry = DeferredRegistry<String, String>()
		registry.register("sword") { "Sword" }

		assertFailsWith<IllegalArgumentException> {
			registry.register("sword") { "Another Sword" }
		}
	}

	@Test
	fun `registering after finalization fails`() {
		val registry = DeferredRegistry<String, String>()
		registry.registerAll { _, factory -> factory() }

		assertFailsWith<IllegalStateException> {
			registry.register("sword") { "Sword" }
		}
	}

	@Test
	fun `finalizing twice fails`() {
		val registry = DeferredRegistry<String, String>()
		registry.registerAll { _, factory -> factory() }

		assertFailsWith<IllegalStateException> {
			registry.registerAll { _, factory -> factory() }
		}
	}

	@Test
	fun `entries registered after the first flush are not silently dropped`() {
		val registry = DeferredRegistry<String, String>()
		val first = registry.register("sword") { "Sword" }
		registry.registerAll { _, factory -> factory() }

		assertTrue(first.isBound)
		assertEquals(1, registry.size)
	}

	@Test
	fun `onBind is invoked once the entry is bound`() {
		val registry = DeferredRegistry<String, String>()
		val entry = registry.register("sword") { "Sword" }

		var received: String? = null
		entry.onBind { value -> received = value }
		assertEquals(null, received)

		registry.registerAll { _, factory -> factory() }

		assertEquals("Sword", received)
	}

	@Test
	fun `onBind invokes listener immediately when already bound`() {
		val registry = DeferredRegistry<String, String>()
		val entry = registry.register("sword") { "Sword" }
		registry.registerAll { _, factory -> factory() }

		var received: String? = null
		entry.onBind { value -> received = value }

		assertEquals("Sword", received)
	}

	@Test
	fun `getEntry returns null for unknown keys`() {
		val registry = DeferredRegistry<String, String>()
		registry.register("sword") { "Sword" }

		assertEquals(null, registry.getEntry("shield"))
		assertTrue(registry.containsKey("sword"))
		assertFalse(registry.containsKey("shield"))
	}

	@Test
	fun `keys and forEach reflect registration order`() {
		val registry = DeferredRegistry<String, String>()
		registry.register("a") { "A" }
		registry.register("b") { "B" }
		registry.register("c") { "C" }

		assertEquals(listOf("a", "b", "c"), registry.keys.toList())

		registry.registerAll { _, factory -> factory() }

		val collected = mutableListOf<Pair<String, String>>()
		registry.forEach { key, entry -> collected += key to entry.get() }

		assertEquals(listOf("a" to "A", "b" to "B", "c" to "C"), collected)
	}

	@Test
	fun `isFinalized reflects registry lifecycle`() {
		val registry = DeferredRegistry<String, String>()
		assertFalse(registry.isFinalized)

		registry.registerAll { _, factory -> factory() }

		assertTrue(registry.isFinalized)
	}
}
