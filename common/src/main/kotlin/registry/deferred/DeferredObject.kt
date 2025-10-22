/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.registry.deferred

import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class DeferredObject<T>(val key: ResourceKey<*>) : Supplier<T> {
	private var holder: Holder<*>? = null

	init {
		bind<Any>(false)
	}

	override fun get(): T {
		bind<Any>(true)
		if (this.holder == null) {
			throw NullPointerException("Trying to access unbound value: " + this.key)
		}

		@Suppress("UNCHECKED_CAST")
		return this.holder!!.value() as T
	}

	fun <R> bind(throwOnMissingRegistry: Boolean) {
		if (this.holder != null) return

		@Suppress("UNCHECKED_CAST")
		val registry = this.registry as Registry<R>?
		if (registry != null) {
			@Suppress("UNCHECKED_CAST")
			this.holder = registry.get(this.key as ResourceKey<R>).orElse(null)
		} else check(!throwOnMissingRegistry) { "Registry not present for " + this + ": " + this.key.registry() }
	}

	val registryKey: ResourceKey<out Registry<*>>
		get() = key.registryKey()

	val registry: Registry<*>?
		get() = BuiltInRegistries.REGISTRY.getValue(this.key.registry())

	val id: ResourceLocation
		get() = this.key.location()

	val isBound: Boolean
		get() {
			bind<Any>(false)
			return this.holder != null && this.holder!!.isBound
		}
}
