/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.utils.serviceloader

import dev.pandasystems.pandalib.game.minecraft
import java.util.*

inline fun <reified T> loadFirstService(): T = ServiceLoader.load(T::class.java)
	.findFirst().orElseThrow { NullPointerException("Failed to load service for " + T::class.java.name) }

inline fun <reified T> lazyLoadFirstService(): Lazy<T> = lazy { loadFirstService<T>() }

inline fun <reified T : VersionLoaded> loadVersionService(): T = ServiceLoader.load(T::class.java)
	.find { it.compatibleVersion == minecraft.version }
	?: throw IllegalStateException("Failed to load service for " + T::class.java.name + " because no compatible version was found")

inline fun <reified T : VersionLoaded> lazyLoadVersionService(): Lazy<T> = lazy { loadVersionService<T>() }