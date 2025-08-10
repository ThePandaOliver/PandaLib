/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.api.registry

import net.minecraft.core.Registry
import net.minecraft.network.syncher.EntityDataSerializer
import net.neoforged.neoforge.registries.NeoForgeRegistries

object PandaLibRegistriesImpl {
	actual val ENTITY_DATA_SERIALIZERS: Registry<EntityDataSerializer<*>>
		get() = NeoForgeRegistries.ENTITY_DATA_SERIALIZERS
}