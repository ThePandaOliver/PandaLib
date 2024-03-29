package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.ElementHolder;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigCategoryList extends ElementHolder {
	public static final int COLLAPSED_SIZE = 24;
	public static final int OPEN_SIZE = 100;

	private static final int MENU_DRAW_EDGE_COLOR = new Color(0, 0, 0, 150).getRGB();
	private static final int MENU_DRAW_EDGE_HIGHLIGHT_COLOR = new Color(100, 100, 100, 150).getRGB();

	private final ConfigMenu<?> configMenu;
	public final Set<AbstractConfigCategory> categories = new HashSet<>();

	public ConfigCategoryList(ConfigMenu<?> configMenu) {
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