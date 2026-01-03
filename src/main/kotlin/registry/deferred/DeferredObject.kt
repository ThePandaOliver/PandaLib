/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.registry.deferred

import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
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

	val id: Identifier
		get() = this.key.identifier()

	val isBound: Boolean
		get() {
			bind<Any>(false)
			return this.holder != null && this.holder!!.isBound
		}
}
