package me.pandamods.pandalib.api.client.screen.converters;

import me.pandamods.pandalib.api.client.screen.PLRenderable;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.gui.components.Renderable;

import java.util.Objects;

/**
 * A class that converts a Renderable object to a PLRenderable object.
 */
public class RenderableConverter implements PLRenderable {
	private final Renderable renderable;

	public RenderableConverter(Renderable renderable) {
		this.renderable = renderable;
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderable.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null) return false;
		return Objects.equals(renderable, object);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(renderable);
	}
}
