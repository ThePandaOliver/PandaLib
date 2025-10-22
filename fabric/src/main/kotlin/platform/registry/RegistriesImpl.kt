/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform.registry

import com.google.auto.service.AutoService
import com.mojang.serialization.Lifecycle
import dev.pandasystems.pandalib.pandalibModid
import dev.pandasystems.pandalib.registry.RegistriesPlatform
import dev.pandasystems.pandalib.registry.deferred.DeferredRegister
import dev.pandasystems.pandalib.registry.deferred.PandaLibRegistry
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
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

		private fun <T> key(name: String): ResourceKey<Registry<T>> {
			return ResourceKey.createRegistryKey<T>(resourceLocation(pandalibModid, name))
		}
	}

	init {
		entityDataSerializers.event.register { key, value, _ ->
			FabricTrackedDataRegistry.register(key.location(), value)
		}
	}
}