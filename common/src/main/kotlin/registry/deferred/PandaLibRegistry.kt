/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.registry.deferred

import com.mojang.serialization.Lifecycle
import dev.pandasystems.pandalib.listener.Listener
import dev.pandasystems.pandalib.listener.ListenerFactory
import net.minecraft.core.Holder
import net.minecraft.core.MappedRegistry
import net.minecraft.core.RegistrationInfo
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

class PandaLibRegistry<T : Any>(key: ResourceKey<Registry<T>>, registryLifecycle: Lifecycle, hasIntrusiveHolders: Boolean) : MappedRegistry<T>(key, registryLifecycle, hasIntrusiveHolders) {
	constructor(key: ResourceKey<Registry<T>>, registryLifecycle: Lifecycle) : this(key, registryLifecycle, false)

	val listener: Listener<(key: ResourceKey<T>, value: T, registrationInfo: RegistrationInfo) -> Unit> = ListenerFactory.create()

	override fun register(
		key: ResourceKey<T>,
		value: T,
		registrationInfo: RegistrationInfo
	): Holder.Reference<T> {
		listener.invoker(key, value, registrationInfo)
		return super.register(key, value, registrationInfo)
	}
}
