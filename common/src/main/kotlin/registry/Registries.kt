/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import dev.pandasystems.pandalib.utils.loadFirstService
import net.minecraft.core.Registry
import net.minecraft.network.syncher.EntityDataSerializer

@OptIn(InternalPandaLibApi::class)
val ENTITY_DATA_SERIALIZERS_REGISTRY: Registry<EntityDataSerializer<*>> get() = registries.entityDataSerializers

@InternalPandaLibApi
val registries = loadFirstService<RegistriesPlatform>()

interface RegistriesPlatform {
	val entityDataSerializers: Registry<EntityDataSerializer<*>>
}
