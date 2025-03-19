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

package dev.pandasystems.pandalib.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;
import java.util.function.Function;

public class PLRenderType {
	private static final Function<Double, RenderType.CompositeRenderType> DEBUG_LINE_STRIP = Util.memoize((double_) ->
			RenderType.create("debug_lines", DefaultVertexFormat.POSITION_COLOR,
					VertexFormat.Mode.DEBUG_LINES, 1536, RenderType.CompositeState
							.builder()
							.setShaderState(RenderType.POSITION_COLOR_SHADER)
							.setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(double_)))
							.setTransparencyState(RenderType.NO_TRANSPARENCY)
							.setCullState(RenderType.NO_CULL)
							.createCompositeState(false)));

	public static RenderType debugLines(double lineWidth) {
		return DEBUG_LINE_STRIP.apply(lineWidth);
	}
}
