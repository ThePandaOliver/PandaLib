/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.registry

import dev.pandasystems.pandalib.core.platform.registryHelper
import net.minecraft.core.Registry

@Suppress("unused")
object RegistryRegister {
	@JvmStatic
	fun <T> register(registry: Registry<T>): Registry<T> {
		registryHelper.registerNewRegistry<T>(registry)
		return registry
	}
}
