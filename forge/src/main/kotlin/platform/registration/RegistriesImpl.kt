/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.forge.platform.registration

import com.google.auto.service.AutoService
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Lifecycle
import dev.pandasystems.pandalib.registry.RegistriesPlatform
import net.minecraft.Util
import net.minecraft.core.*
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.RandomSource
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry
import java.util.*
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

@AutoService(RegistriesPlatform::class)
class RegistriesImpl : RegistriesPlatform {
	override val entityDataSerializers: Registry<EntityDataSerializer<*>>
		get() = ForgeRegistry(ForgeRegistries.ENTITY_DATA_SERIALIZERS.get())
}

class ForgeRegistry<T>(val forgeRegistry: IForgeRegistry<T>) : WritableRegistry<T> {
	override fun register(
		key: ResourceKey<T?>,
		value: T & Any,
		lifecycle: Lifecycle
	): Holder.Reference<T?> {
		forgeRegistry.register(key.location(), value)
		return forgeRegistry.getDelegateOrThrow(key.location())
	}

	override fun isEmpty(): Boolean {
		return forgeRegistry.isEmpty
	}

	override fun createRegistrationLookup(): HolderGetter<T> {
		return object : HolderGetter<T> {
			override fun get(resourceKey: ResourceKey<T>): Optional<Holder.Reference<T>> {
				return forgeRegistry.getDelegate(resourceKey)
			}

			override fun get(tagKey: TagKey<T>): Optional<HolderSet.Named<T?>?> {
				return TODO("Not yet implemented")
			}
		}
	}

	override fun key(): ResourceKey<out Registry<T>> {
		return forgeRegistry.registryKey
	}

	override fun getKey(value: T & Any): ResourceLocation? {
		return forgeRegistry.getKey(value)
	}

	override fun getResourceKey(value: T & Any): Optional<ResourceKey<T>> {
		return forgeRegistry.getResourceKey(value)
	}

	override fun getId(value: T?): Int {
		return forgeRegistry.values.indexOf(value)
	}

	override fun get(key: ResourceKey<T>?): T? {
		@Suppress("UNCHECKED_CAST")
		return key?.let { forgeRegistry.getValue(it.location()) as T? }
	}

	override fun get(name: ResourceLocation?): T? {
		@Suppress("UNCHECKED_CAST")
		return forgeRegistry.getValue(name) as T?
	}

	override fun lifecycle(value: T & Any): Lifecycle {
		return Lifecycle.stable()
	}

	override fun registryLifecycle(): Lifecycle {
		return Lifecycle.stable()
	}

	override fun keySet(): Set<ResourceLocation> {
		return forgeRegistry.keys
	}

	override fun entrySet(): Set<Map.Entry<ResourceKey<T>, T>> {
		return forgeRegistry.entries
	}

	override fun registryKeySet(): Set<ResourceKey<T>> {
		return forgeRegistry.keys.map { ResourceKey.create(key(), it) }.toSet()
	}

	override fun getRandom(random: RandomSource): Optional<Holder.Reference<T>> {
		return Util.getRandomSafe(forgeRegistry.keys.toList(), random).map { forgeRegistry.getDelegate(it).getOrNull() }
	}

	override fun containsKey(name: ResourceLocation): Boolean {
		return forgeRegistry.containsKey(name)
	}

	override fun containsKey(key: ResourceKey<T>): Boolean {
		return forgeRegistry.containsKey(key.location())
	}

	override fun freeze(): Registry<T> {
		return this
	}

	override fun createIntrusiveHolder(value: T & Any): Holder.Reference<T> {
		return forgeRegistry.getDelegate(value).get()
	}

	override fun getHolder(id: Int): Optional<Holder.Reference<T>> {
		return forgeRegistry.getDelegate(forgeRegistry.keys.toList()[id])
	}

	override fun getHolder(key: ResourceKey<T>): Optional<Holder.Reference<T>> {
		return forgeRegistry.getDelegate(key.location())
	}

	override fun wrapAsHolder(value: T & Any): Holder<T> {
		return forgeRegistry.getDelegateOrThrow(value)
	}

	override fun holders(): Stream<Holder.Reference<T>> {
		return forgeRegistry.values.stream().map { forgeRegistry.getDelegateOrThrow(it) }
	}

	override fun getTag(key: TagKey<T?>): Optional<HolderSet.Named<T?>?> {
		TODO("Not yet implemented")
	}

	override fun getOrCreateTag(key: TagKey<T?>): HolderSet.Named<T?> {
		TODO("Not yet implemented")
	}

	override fun getTags(): Stream<Pair<TagKey<T?>?, HolderSet.Named<T?>?>?> {
		TODO("Not yet implemented")
	}

	override fun getTagNames(): Stream<TagKey<T?>?> {
		TODO("Not yet implemented")
	}

	override fun resetTags() {
		TODO("Not yet implemented")
	}

	override fun bindTags(tagMap: Map<TagKey<T?>?, List<Holder<T?>?>?>) {
		TODO("Not yet implemented")
	}

	override fun holderOwner(): HolderOwner<T?> {
		TODO("Not yet implemented")
	}

	override fun asLookup(): HolderLookup.RegistryLookup<T?> {
		TODO("Not yet implemented")
	}

	override fun byId(id: Int): T? {
		return forgeRegistry.values.toList()[id]
	}

	override fun size(): Int {
		return forgeRegistry.values.size
	}

	override fun iterator(): MutableIterator<T?> {
		return forgeRegistry.values.iterator()
	}
}