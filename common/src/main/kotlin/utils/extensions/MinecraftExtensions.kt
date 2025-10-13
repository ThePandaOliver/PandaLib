/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

@file:JvmName("MCUtils")

package dev.pandasystems.pandalib.utils.extensions

import dev.pandasystems.pandalib.PandaLib
import net.minecraft.resources.ResourceLocation

fun resourceLocation(namespace: String, path: String): ResourceLocation = ResourceLocation(namespace, path)

internal fun resourceLocation(path: String): ResourceLocation = ResourceLocation(PandaLib.MOD_ID, path)