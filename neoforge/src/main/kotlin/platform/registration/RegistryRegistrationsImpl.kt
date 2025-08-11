/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.platform.registration

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.platform.RegistryRegistrations
import net.minecraft.core.Registry
import net.minecraft.network.syncher.EntityDataSerializer
import net.neoforged.neoforge.registries.NeoForgeRegistries

@AutoService(RegistryRegistrations::class)
class RegistryRegistrationsImpl : RegistryRegistrations {
	override val entityDataSerializers: Registry<EntityDataSerializer<*>>
		get() = NeoForgeRegistries.ENTITY_DATA_SERIALIZERS
}