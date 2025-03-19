/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.mixin;

import com.mojang.blaze3d.vertex.VertexFormat;
import dev.pandasystems.pandalib.client.render.TriangleRenderType;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderType.CompositeRenderType.class)
public class CompositeRenderTypeMixin {
	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, RenderType.CompositeState state, CallbackInfo ci) {
		if (mode != VertexFormat.Mode.TRIANGLES) {
			TriangleRenderType.RENDER_TYPES.put((RenderType.CompositeRenderType) (Object) this, RenderType.create(name, format, VertexFormat.Mode.TRIANGLES, bufferSize, affectsCrumbling, sortOnUpload, state));
		}
	}
}
