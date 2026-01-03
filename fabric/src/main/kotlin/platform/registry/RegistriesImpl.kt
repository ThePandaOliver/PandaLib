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

package dev.pandasystems.pandalib.fabric.platform.registry

import com.google.auto.service.AutoService
import com.mojang.serialization.Lifecycle
import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.registry.RegistriesPlatform
import dev.pandasystems.pandalib.registry.deferred.DeferredRegister
import dev.pandasystems.pandalib.registry.deferred.PandaLibRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricTrackedDataRegistry
import net.minecraft.core.Registry
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.resources.ResourceKey

@AutoService(RegistriesPlatform::class)
class RegistriesImpl : RegistriesPlatform {
	override val entityDataSerializers: PandaLibRegistry<EntityDataSerializer<*>> =
		DeferredRegister.registerNewRegistry(PandaLibRegistry(ENTITY_DATA_SERIALIZERS, Lifecycle.stable()))

	companion object Keys {
		val ENTITY_DATA_SERIALIZERS: ResourceKey<Registry<EntityDataSerializer<*>>> = key("entity_data_serializers")

		private fun <T : Any> key(name: String): ResourceKey<Registry<T>> {
			return ResourceKey.createRegistryKey<T>(PandaLib.identifier(name))
		}
	}

	init {
		entityDataSerializers.event.register { key, value, _ ->
			FabricTrackedDataRegistry.register(key.identifier(), value)
		}
	}
}