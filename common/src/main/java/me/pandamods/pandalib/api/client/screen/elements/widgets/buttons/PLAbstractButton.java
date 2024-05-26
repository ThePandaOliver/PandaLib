package me.pandamods.pandalib.api.client.screen.elements.widgets.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import me.pandamods.pandalib.api.client.screen.PLRenderable;
import me.pandamods.pandalib.api.client.screen.elements.AbstractUIElement;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import java.awt.*;

public abstract class PLAbstractButton extends AbstractUIElement implements PLRenderable {
	private ButtonClickConsumer clickListener;
	private ButtonClickConsumer releaseListener;

	public abstract Component getText();

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		if (this.isVisible()) {
			checkHoverState(mouseX, mouseY);
			Minecraft minecraft = Minecraft.getInstance();
			RenderSystem.enableBlend();
			RenderSystem.enableDepthTest();
			renderTexture(guiGraphics);
			int i = this.isActive() ? Color.white.getRGB() : Color.gray.getRGB();
			this.renderString(guiGraphics, minecraft.font, i);
		}
	}

	public void renderString(PLGuiGraphics guiGraphics, Font font, int color) {
		guiGraphics.drawScrollingText(font, getText(), minX() + 2, minY(), maxX() - 2, maxY(), color);
	}

	public void renderTexture(PLGuiGraphics guiGraphics) {
		guiGraphics.blitNineSliced(AbstractWidget.WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
				20, 4, 200, 20, 0, this.getTextureY());
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

	public void playSound() {
		SoundManager soundManager = Minecraft.getInstance().getSoundManager();
		soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
	}

	public void setClickListener(ButtonClickConsumer clickListener) {
		this.clickListener = clickListener;
	}

	public void setReleaseListener(ButtonClickConsumer releaseListener) {
		this.releaseListener = releaseListener;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.isActiveAndVisible()) {
    		this.playSound();
			if (this.clickListener != null)
				this.clickListener.onClick(getClickType(button));
    		return true;
    	}
		return true;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (this.isActiveAndVisible() && this.releaseListener != null) {
    		this.releaseListener.onClick(getClickType(button));
    		return true;
    	}
		return true;
	}

	protected ClickType getClickType(int button) {
		return switch (button) {
			case 0 -> ClickType.LEFT_CLICK;
			case 1 -> ClickType.RIGHT_CLICK;
			case 2 -> ClickType.MIDDLE_CLICK;
			default -> ClickType.NONE;
		};
	}

	public interface ButtonClickConsumer {
		void onClick(ClickType clickType);
	}

	public enum ClickType {
		RIGHT_CLICK,
		MIDDLE_CLICK,
		LEFT_CLICK,
		NONE
	}
}
