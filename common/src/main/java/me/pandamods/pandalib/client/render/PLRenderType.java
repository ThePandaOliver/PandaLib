package me.pandamods.pandalib.client.render;

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
