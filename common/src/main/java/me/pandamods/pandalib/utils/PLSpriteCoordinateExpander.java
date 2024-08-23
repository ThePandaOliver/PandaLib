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

package me.pandamods.pandalib.utils;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@Environment(value=EnvType.CLIENT)
public class PLSpriteCoordinateExpander implements VertexConsumer {
    private final VertexConsumer delegate;
    private final TextureAtlasSprite sprite;

    public PLSpriteCoordinateExpander(VertexConsumer delegate, TextureAtlasSprite sprite) {
        this.delegate = delegate;
        this.sprite = sprite;
    }

	#if MC_VER >= MC_1_21
    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        this.delegate.addVertex(x, y, z);
		return this;
    }

    @Override
    public VertexConsumer setColor(int red, int green, int blue, int alpha) {
        this.delegate.setColor(red, green, blue, alpha);
		return this;
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
		this.delegate.setUv(this.sprite.getU(u), this.sprite.getV(v));
		return this;
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        this.delegate.setUv1(u, v);
		return this;
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        this.delegate.setUv2(u, v);
		return this;
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        this.delegate.setNormal(x, y, z);
		return this;
    }

	@Override
	public void addVertex(float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, float normalX, float normalY, float normalZ) {
		this.delegate.addVertex(x, y, z, color, this.sprite.getU(u), this.sprite.getV(v),
				packedOverlay, packedLight, normalX, normalY, normalZ);
	}
	#else
		@Override
		public VertexConsumer vertex(double x, double y, double z) {
			this.delegate.vertex(x, y, z);
			return this;
		}

		@Override
		public VertexConsumer color(int red, int green, int blue, int alpha) {
			this.delegate.color(red, green, blue, alpha);
			return this;
		}

		@Override
		public VertexConsumer uv(float u, float v) {
			this.delegate.uv(u, v);
			return this;
		}

		@Override
		public VertexConsumer overlayCoords(int u, int v) {
			this.delegate.overlayCoords(u, v);
			return this;
		}

		@Override
		public VertexConsumer uv2(int u, int v) {
			this.delegate.uv2(u, v);
			return this;
		}

		@Override
		public VertexConsumer normal(float x, float y, float z) {
			this.delegate.normal(x, y, z);
			return this;
		}

		@Override
		public void endVertex() {
			this.delegate.endVertex();
		}

		@Override
		public void defaultColor(int defaultR, int defaultG, int defaultB, int defaultA) {
			this.delegate.defaultColor(defaultR, defaultG, defaultB, defaultA);
		}

		@Override
		public void unsetDefaultColor() {
			this.delegate.unsetDefaultColor();
		}

		@Override
		public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float texU, float texV, int overlayUV, int lightmapUV,
						   float normalX, float normalY, float normalZ) {
			this.delegate.vertex(x, y, z, red, green, blue, alpha, this.sprite.getU(texU), this.sprite.getV(texV),
					overlayUV, lightmapUV, normalX, normalY, normalZ);
		}
	#endif
}