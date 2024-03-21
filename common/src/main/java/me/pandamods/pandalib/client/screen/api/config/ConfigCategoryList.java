package me.pandamods.pandalib.client.screen.api.config;

import me.pandamods.pandalib.client.screen.api.Widget;
import me.pandamods.pandalib.client.screen.api.widgets.buttons.AbstractToggleButton;
import me.pandamods.pandalib.utils.animation.interpolation.NumberInterpolator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class ConfigCategoryList extends Widget {
	public static final int COLLAPSED_SIZE = 24;
	public static final int OPEN_SIZE = 100;

	private static final int MENU_DRAW_EDGE_COLOR = new Color(0, 0, 0, 150).getRGB();
	private static final int MENU_DRAW_EDGE_HIGHLIGHT_COLOR = new Color(100, 100, 100, 150).getRGB();

	private final NumberInterpolator widthInterpolator = new NumberInterpolator(COLLAPSED_SIZE).setDuration(1f);
	private final MenuButton menuButton = new MenuButton(2, 2);
	private final ConfigMenu configMenu;

	public ConfigCategoryList(ConfigMenu configMenu) {
		this.configMenu = configMenu;
	}

	@Override
	protected void init() {
		this.addRenderableWidget(menuButton);
		super.init();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		widthInterpolator.setTarget(menuButton.state ? OPEN_SIZE : COLLAPSED_SIZE);
		widthInterpolator.update();
		menuButton.setX(widthInterpolator.getAsInt() - 22);
		guiGraphics.fill(maxX(), minY(), maxX() + 1, maxY(), MENU_DRAW_EDGE_COLOR);
		guiGraphics.fill(maxX() + 1, minY(), maxX() + 2, maxY(), MENU_DRAW_EDGE_HIGHLIGHT_COLOR);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public int localX() {
		return 0;
	}

	@Override
	public int localY() {
		return 0;
	}

	@Override
	public int width() {
		return widthInterpolator.getAsInt();
	}

	@Override
	public int height() {
		return this.getScreen().height;
	}

	public static class MenuButton extends AbstractToggleButton {
		public MenuButton(int x, int y) {
			super(x, y, 20, 20, false, Component.empty());
		}

		@Override
		public Component getMessage() {
			return Component.translatable(state ? "gui.pandalib.close_draw" : "gui.pandalib.open_draw");
		}

		@Override
		protected void renderScrollingString(GuiGraphics guiGraphics, Font font, int width, int color) {
			renderScrollingString(guiGraphics, font, Component.literal(state ? "<" : "☰"),
					this.minX() + width, this.minY(), this.maxX() - width, this.maxY(), color);
		}
	}
}
