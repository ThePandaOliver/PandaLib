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

import com.mojang.serialization.Lifecycle
import dev.pandasystems.pandalib.utils.Event
import dev.pandasystems.pandalib.utils.event
import net.minecraft.core.Holder
import net.minecraft.core.MappedRegistry
import net.minecraft.core.RegistrationInfo
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

class PandaLibRegistry<T : Any>(key: ResourceKey<Registry<T>>, registryLifecycle: Lifecycle, hasIntrusiveHolders: Boolean) : MappedRegistry<T>(key, registryLifecycle, hasIntrusiveHolders) {
	constructor(key: ResourceKey<Registry<T>>, registryLifecycle: Lifecycle) : this(key, registryLifecycle, false)

	val event: Event<(key: ResourceKey<T>, value: T, registrationInfo: RegistrationInfo) -> Unit> = event()

	override fun register(
		key: ResourceKey<T>,
		value: T,
		registrationInfo: RegistrationInfo
	): Holder.Reference<T> {
		event.invoker(key, value, registrationInfo)
		return super.register(key, value, registrationInfo)
	}
}
