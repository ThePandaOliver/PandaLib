/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.pandamods.pandalib.registry

import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class DeferredObject<T>(@JvmField val key: ResourceKey<*>) : Supplier<T> {
	private var holder: Holder<*>? = null

	init {
		bind<Any>(false)
	}

	override fun get(): T {
		bind<Any>(true)
		if (this.holder == null) {
			throw NullPointerException("Trying to access unbound value: " + this.key)
		}

		return this.holder!!.value() as T
	}

	fun <R> bind(throwOnMissingRegistry: Boolean) {
		if (this.holder != null) return

		val registry = this.registry as Registry<R>
		if (registry != null) {
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
			return this.holder != null && this.holder!!.isBound()
		}
}
