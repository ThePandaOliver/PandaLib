package me.pandamods.pandalib.client.config.screen.widgets;

import me.pandamods.pandalib.client.config.ConfigCategoryImpl;
import me.pandamods.pandalib.client.config.screen.ConfigScreen;
import me.pandamods.pandalib.client.screen.widgets.ScrollableWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigCategoryHistory extends ScrollableWidget {
	private final ConfigScreen configScreen;
	private GridLayout gridLayout;

	private List<ConfigCategoryImpl> history = new ArrayList<>();

	public ConfigCategoryHistory(ConfigScreen parent) {
		super(parent);
		this.configScreen = parent;
	}



	private void arrange() {
		if (gridLayout != null) {
			gridLayout.arrangeElements();
			FrameLayout.alignInRectangle(gridLayout, this.getX() + 2, this.getY(), this.getWidth(), this.getHeight(), 0, 0.5f);
		}
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
		arrange();
	}

	@Override
	public void initWidget() {
		this.gridLayout = new GridLayout();
		this.gridLayout.defaultCellSetting();

		for (int i = 0; i < history.size(); i++) {
			gridLayout.addChild(new HistoryButton(this.getHeight(), history.get(i)), 0, i);
		}
		gridLayout.addChild(new HistoryButton(this.getHeight(), configScreen.getCategory()), 0, history.size());

		arrange();
		gridLayout.visitWidgets(this::addRenderableWidget);
	}

	@Override
	public int getMaxScrollDistanceX() {
		return gridLayout.getWidth() + 2;
	}

	@Override
	public int getMaxScrollDistanceY() {
		return 0;
	}

	public void setHistory(List<ConfigCategoryImpl> predecessors) {
		history = new ArrayList<>(predecessors);
		rebuildWidgets();
	}

	public class HistoryButton extends AbstractButton {
		private final ConfigCategoryImpl category;

		public HistoryButton(int height, ConfigCategoryImpl category) {
			super(0, 0, 0, height, category.getTitle());
			this.category = category;
		}

		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			Font font = Minecraft.getInstance().font;
			Component text = getMessage().copy().append(" > ");
			this.width = font.width(text);
			int textWidth = font.width(getMessage());
			int y = getY() + (height - font.lineHeight) / 2;
			int color = Color.white.getRGB();
			guiGraphics.drawString(font, text, getX(), y, color);
			if (this.isHoveredOrFocused()) {
				y += font.lineHeight + 1;
				guiGraphics.fill(this.getX() + 1, y, this.getX() + textWidth - 1, y + 2, color);
			}
		}

		@Override
		public void onPress() {
			ConfigCategoryHistory.this.configScreen.setCategory(category);
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

		}
	}
}
