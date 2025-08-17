/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.core.platform.registry

import dev.pandasystems.pandalib.api.registry.deferred.DeferredObject
import dev.pandasystems.pandalib.core.utils.loadFirstService
import net.minecraft.core.Registry
import java.util.function.Supplier

interface DeferredRegisterHelper {
	fun <T> registerObject(deferredObject: DeferredObject<out T>, supplier: Supplier<out T>)
	fun <T> registerNewRegistry(registry: Registry<T>)
}

@JvmField
val deferredRegisterHelper = loadFirstService<DeferredRegisterHelper>()