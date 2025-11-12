/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.forge.platform.registration

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.registry.RegistriesPlatform
import net.minecraft.core.Registry
import net.minecraft.network.syncher.EntityDataSerializer

@AutoService(RegistriesPlatform::class)
class RegistriesImpl : RegistriesPlatform {
	override val entityDataSerializers: Registry<EntityDataSerializer<*>>
		get() = TODO("Not yet implemented")
}