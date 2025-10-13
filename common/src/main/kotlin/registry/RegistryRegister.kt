/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.platform.registry.deferredRegisterHelper
import net.minecraft.core.Registry

@Suppress("unused")
object RegistryRegister {
	@JvmStatic
	fun <T, R : Registry<T>> register(registry: R): R {
		deferredRegisterHelper.registerNewRegistry(registry)
		return registry
	}
}
