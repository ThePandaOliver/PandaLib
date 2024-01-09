package me.pandamods.pandalib.client.screen.widgets.button;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ButtonIcon extends AbstractButton {
	private final ResourceLocation resourceLocation;
	private final int textureWidth;
	private final int textureHeight;
	private Runnable runnable;

	public ButtonIcon(ResourceLocation resourceLocation, int x, int y, int width, int height, int textureWidth, int textureHeight, Component message) {
		super(x, y, width, height, message);
		this.resourceLocation = resourceLocation;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.setTooltip(Tooltip.create(message));
	}

	public ButtonIcon(ResourceLocation resourceLocation, int x, int y, int textureWidth, int textureHeight, Component message) {
		this(resourceLocation, x, y, 20, 20, textureWidth, textureHeight, message);
	}

	public void setPressListener(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void onPress() {
		if (runnable != null)
			runnable.run();
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
		guiGraphics.blit(resourceLocation, getX() + 2, getY() + 2, 0, 0,
				getWidth() - 4, getHeight() - 4, textureWidth, textureHeight);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

	}

	@Override
	public void renderString(GuiGraphics guiGraphics, Font font, int color) {
	}
}
