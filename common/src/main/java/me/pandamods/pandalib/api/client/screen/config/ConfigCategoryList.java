package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.ElementHolder;
import me.pandamods.pandalib.api.client.screen.widget.buttons.AbstractToggleButton;
import me.pandamods.pandalib.core.utils.animation.interpolation.NumberInterpolator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class ConfigCategoryList extends ElementHolder {
	public static final int COLLAPSED_SIZE = 24;
	public static final int OPEN_SIZE = 100;

	private static final int MENU_DRAW_EDGE_COLOR = new Color(0, 0, 0, 150).getRGB();
	private static final int MENU_DRAW_EDGE_HIGHLIGHT_COLOR = new Color(100, 100, 100, 150).getRGB();

	private final ConfigMenu configMenu;

	public ConfigCategoryList(ConfigMenu configMenu) {
		this.configMenu = configMenu;
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.fill(maxX(), minY(), maxX() + 1, maxY(), MENU_DRAW_EDGE_COLOR);
		guiGraphics.fill(maxX() + 1, minY(), maxX() + 2, maxY(), MENU_DRAW_EDGE_HIGHLIGHT_COLOR);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public int getWidth() {
		return OPEN_SIZE;
	}

	@Override
	public int getHeight() {
		return this.configMenu.height;
	}
}