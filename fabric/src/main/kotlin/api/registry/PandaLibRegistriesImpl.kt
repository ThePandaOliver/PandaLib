/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.api.registry

import net.minecraft.core.Registry
import net.minecraft.network.syncher.EntityDataSerializer

object PandaLibRegistriesImpl {
	actual val ENTITY_DATA_SERIALIZERS: Registry<EntityDataSerializer<*>>
		get() = TODO("Implement a Fabric registry for registering EntityDataSerializers, and then register all entries in with FabricTrackedDataRegistry")
}