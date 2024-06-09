package me.pandamods.pandalib.api.client.screen.elements.widgets.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import me.pandamods.pandalib.api.client.screen.tooltip.PLBelowOrAboveWidgetTooltipPositioner;
import me.pandamods.pandalib.api.client.screen.tooltip.PLMenuTooltipPositioner;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.tooltip.BelowOrAboveWidgetTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractIconButton extends AbstractPLButton {
	private final int size;
	private final WidgetSprites iconSprite;
	private final int textureSize;

	private Tooltip tooltip;

	public AbstractIconButton(int x, int y, int size, Component message, WidgetSprites iconSprite, int textureSize) {
		super(message);
		this.setPosition(x, y);
		this.setSize(size, size);
		this.size = size;
		this.iconSprite = iconSprite;
		this.textureSize = textureSize;
		this.tooltip = Tooltip.create(getMessage());
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.checkHoverState(mouseX, mouseY);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		guiGraphics.blitSprite(SPRITES.get(this.isActive(), this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		guiGraphics.blit(iconSprite.get(this.isActive(), this.isHoveredOrFocused()), this.getX() + 2, this.getY() + 2, 0,
				0, 0, this.size - 4, this.size - 4, this.textureSize, this.textureSize);

		if (this.isHovered() && this.tooltip != null) {
			Minecraft.getInstance().screen.setTooltipForNextRenderPass(this.tooltip, createTooltipPositioner(), this.isFocused());
		}
	}

	protected ClientTooltipPositioner createTooltipPositioner() {
		return !this.isHovered() && this.isFocused() && Minecraft.getInstance().getLastInputType().isKeyboard() ?
				new PLBelowOrAboveWidgetTooltipPositioner(this) : new PLMenuTooltipPositioner(this);
	}
}
