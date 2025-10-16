/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.utils

import com.mojang.blaze3d.vertex.PoseStack
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

@Suppress("unused")
object MathUtils {
	@JvmOverloads
	@JvmStatic
	fun lerpMatrix(matrix: Matrix4f, other: Matrix4f, alpha: Float, dist: Matrix4f = Matrix4f()): Matrix4f {
		val translation = Vector3f()
		val rotation = Quaternionf()
		val scale = Vector3f()

		val otherTranslation = Vector3f()
		val otherRotation = Quaternionf()
		val otherScale = Vector3f()

		matrix.getTranslation(translation)
		matrix.getUnnormalizedRotation(rotation)
		matrix.getScale(scale)

		other.getTranslation(otherTranslation)
		other.getUnnormalizedRotation(otherRotation)
		other.getScale(otherScale)

		translation.lerp(otherTranslation, alpha)
		rotation.slerp(otherRotation, alpha)
		scale.lerp(otherScale, alpha)

		return dist.translationRotateScale(translation, rotation, scale)
	}

	@JvmStatic
	fun rotateVector(target: Vector3f, rotation: Vector3f): Vector3f {
		return target.rotateZ(rotation.z).rotateY(rotation.y).rotateX(rotation.x)
	}

	@JvmStatic
	fun rotateVector(stack: PoseStack, rotation: Vector3f): PoseStack {
		val quaternionf = Quaternionf().rotateZYX(rotation.z, rotation.y, rotation.x)
		stack.mulPose(quaternionf)
		return stack
	}

	@JvmStatic
	fun rotateAroundOrigin(target: Vector3f, origin: Vector3f, rotation: Vector3f): Vector3f {
		target.add(origin)
		rotateVector(target, rotation)
		return target.sub(origin)
	}

	@JvmStatic
	fun rotateAroundOrigin(stack: PoseStack, origin: Vector3f, rotation: Vector3f): PoseStack {
		stack.translate(origin.x, origin.y, origin.z)
		rotateVector(stack, rotation)
		stack.translate(-origin.x, -origin.y, -origin.z)
		return stack
	}
}
