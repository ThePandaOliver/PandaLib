package me.pandamods.pandalib.api.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import me.pandamods.pandalib.api.client.screen.elements.widgets.buttons.AbstractPLButton;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public abstract class AbstractIconButton extends AbstractPLButton {
	private final int size;
	private final ResourceLocation iconLocation;
	private final int textureSize;

	public AbstractIconButton(int x, int y, int size, Component message, ResourceLocation iconLocation, int textureSize) {
		super(message);
		this.setPosition(x, y);
		this.setSize(size, size);
		this.size = size;
		this.iconLocation = iconLocation;
		this.textureSize = textureSize;
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.checkHoverState(mouseX, mouseY);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		guiGraphics.blitNineSliced(AbstractWidget.WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
				20, 4, 200, 20, 0, this.getTextureY());
		guiGraphics.blit(this.iconLocation, this.getX() + 2, this.getY() + 2, 0,
				0, 0, this.size - 4, this.size - 4, this.textureSize, this.textureSize);
	}

	private int getTextureY() {
		int i = 1;
		if (!this.isActive()) {
			i = 0;
		} else if (this.isHoveredOrFocused()) {
			i = 2;
		}

		return 46 + i * 20;
	}
}
