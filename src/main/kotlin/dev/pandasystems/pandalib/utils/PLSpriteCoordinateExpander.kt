/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package dev.pandasystems.pandalib.utils

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.texture.TextureAtlasSprite

@Environment(value = EnvType.CLIENT)
@Suppress("unused")
class PLSpriteCoordinateExpander(private val delegate: VertexConsumer, private val sprite: TextureAtlasSprite) : VertexConsumer {
	override fun addVertex(x: Float, y: Float, z: Float): VertexConsumer {
		this.delegate.addVertex(x, y, z)
		return this
	}

	override fun setColor(red: Int, green: Int, blue: Int, alpha: Int): VertexConsumer {
		this.delegate.setColor(red, green, blue, alpha)
		return this
	}

	override fun setUv(u: Float, v: Float): VertexConsumer {
		this.delegate.setUv(this.sprite.getU(u), this.sprite.getV(v))
		return this
	}

	override fun setUv1(u: Int, v: Int): VertexConsumer {
		this.delegate.setUv1(u, v)
		return this
	}

	override fun setUv2(u: Int, v: Int): VertexConsumer {
		this.delegate.setUv2(u, v)
		return this
	}

	override fun setNormal(x: Float, y: Float, z: Float): VertexConsumer {
		this.delegate.setNormal(x, y, z)
		return this
	}

	override fun addVertex(
		x: Float,
		y: Float,
		z: Float,
		color: Int,
		u: Float,
		v: Float,
		packedOverlay: Int,
		packedLight: Int,
		normalX: Float,
		normalY: Float,
		normalZ: Float
	) {
		this.delegate.addVertex(
			x, y, z, color, this.sprite.getU(u), this.sprite.getV(v),
			packedOverlay, packedLight, normalX, normalY, normalZ
		)
	}
}