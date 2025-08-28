/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform.registry

import com.google.auto.service.AutoService
import com.mojang.serialization.Lifecycle
import dev.pandasystems.pandalib.registry.RegistryRegister
import dev.pandasystems.pandalib.registry.deferred.PandaLibRegistry
import dev.pandasystems.pandalib.PandaLib.resourceLocation
import dev.pandasystems.pandalib.platform.registry.RegistryRegistrations
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricTrackedDataRegistry
import net.minecraft.core.Registry
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.resources.ResourceKey


@AutoService(RegistryRegistrations::class)
class RegistryRegistrationsImpl : RegistryRegistrations {
	override val entityDataSerializers: PandaLibRegistry<EntityDataSerializer<*>> =
		RegistryRegister.register(PandaLibRegistry(ENTITY_DATA_SERIALIZERS, Lifecycle.stable()))

	companion object Keys {
		val ENTITY_DATA_SERIALIZERS: ResourceKey<Registry<EntityDataSerializer<*>>> = key("entity_data_serializers")

		private fun <T> key(name: String): ResourceKey<Registry<T>> {
			return ResourceKey.createRegistryKey<T>(resourceLocation(name))
		}
	}

	init {
		entityDataSerializers.listener.register { key, value, _ ->
			FabricTrackedDataRegistry.register(key.location(), value)
		}
	}
}