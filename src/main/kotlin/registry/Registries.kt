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
