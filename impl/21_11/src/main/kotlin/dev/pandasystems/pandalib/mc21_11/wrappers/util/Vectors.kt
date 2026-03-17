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

package dev.pandasystems.pandalib.mc21_11.wrappers.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3i
import org.joml.Vector3ic

fun Vec3.toPl(): Vector3dc = Vector3d(x, y, z)
fun Vector3dc.toMc(): Vec3 = Vec3(x(), y(), z())

fun Vec3i.toPl() = Vector3i(x, y, z)
fun Vector3ic.toMc() = Vec3i(x(), y(), z())

fun Vector3ic.toBlockPos() = BlockPos(x(), y(), z())