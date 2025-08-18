/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.registry.deferred

import com.mojang.serialization.Lifecycle
import dev.pandasystems.pandalib.api.listener.Listener
import dev.pandasystems.pandalib.api.listener.ListenerFactory
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.MappedRegistry
import net.minecraft.core.RegistrationInfo
import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.tags.TagLoader
import net.minecraft.util.RandomSource
import java.util.Optional
import java.util.stream.Stream

class PandaLibRegistry<T : Any>(key: ResourceKey<Registry<T>>, registryLifecycle: Lifecycle, hasIntrusiveHolders: Boolean) : MappedRegistry<T>(key, registryLifecycle, hasIntrusiveHolders) {
	constructor(key: ResourceKey<Registry<T>>, registryLifecycle: Lifecycle) : this(key, registryLifecycle, false)

	val listener: Listener<(key: ResourceKey<T>, value: T, registrationInfo: RegistrationInfo) -> Unit> = ListenerFactory.create()

	override fun register(
		key: ResourceKey<T>,
		value: T,
		registrationInfo: RegistrationInfo
	): Holder.Reference<T> {
		listener.invoker().invoke(key, value, registrationInfo)
		return super.register(key, value, registrationInfo)
	}
}
