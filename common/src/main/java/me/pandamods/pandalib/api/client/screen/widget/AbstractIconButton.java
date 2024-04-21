package me.pandamods.pandalib.api.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public abstract class AbstractIconButton extends AbstractButton {
	private final int size;
	private final ResourceLocation iconLocation;
	private final int textureSize;

	public AbstractIconButton(int x, int y, int size, Component message, ResourceLocation iconLocation, int textureSize) {
		super(x, y, size, size, message);
		this.size = size;
		this.iconLocation = iconLocation;
		this.textureSize = textureSize;
		this.setTooltip(Tooltip.create(message));
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		guiGraphics.blitNineSliced(WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
				20, 4, 200, 20, 0, this.getTextureY());
		guiGraphics.blit(this.iconLocation, this.getX() + 2, this.getY() + 2, 0,
				0, 0, this.size - 4, this.size - 4, this.textureSize, this.textureSize);
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private int getTextureY() {
		int i = 1;
		if (!this.active) {
			i = 0;
		} else if (this.isHoveredOrFocused()) {
			i = 2;
		}

		return 46 + i * 20;
	}
}
