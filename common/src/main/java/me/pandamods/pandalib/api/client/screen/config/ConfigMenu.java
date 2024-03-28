package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.PLScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ConfigMenu extends PLScreen {
	private final ConfigCategoryList categoryList = new ConfigCategoryList(this);

	protected ConfigMenu(Component title) {
		super(title);
	}

	@Override
	protected void init() {
		this.addElement(categoryList);
		super.init();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderDirtBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}
}