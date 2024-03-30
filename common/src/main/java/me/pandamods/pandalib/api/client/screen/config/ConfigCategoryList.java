package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.ElementHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.*;
import java.util.List;

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
		int i = 0;
		for (AbstractConfigCategory category : categories) {
			this.addRenderableWidget(new Button(0, 22 * i++, this.width, 20, category));
		}
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

	public class Button extends AbstractButton {
		private final AbstractConfigCategory category;

		public Button(int x, int y, int width, int height, AbstractConfigCategory category) {
			super(x, y, width, height, category.getName());
			this.category = category;
		}

		@Override
		public void onPress() {
			ConfigCategoryList.this.configMenu.setCategory(category);
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
			this.defaultButtonNarrationText(narrationElementOutput);
		}

		@Override
		public boolean isActive() {
			return Objects.equals(ConfigCategoryList.this.configMenu.getCategory(), category);
		}
	}
}