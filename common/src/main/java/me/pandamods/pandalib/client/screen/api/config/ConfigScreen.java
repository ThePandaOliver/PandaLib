package me.pandamods.pandalib.client.screen.api.config;

import me.pandamods.pandalib.client.screen.api.PLScreen;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends PLScreen {
	private final Screen parent;
	private final ConfigHolder<?> configHolder;
	private final Object config;
	private final Object defaultConfig;
	private ConfigOptionList optionsList;

	public ConfigScreen(Screen parent, ConfigHolder<?> configHolder, Object config, Object defaultConfig) {
		super(Component.empty());
		this.parent = parent;
		this.configHolder = configHolder;
		this.config = config;
		this.defaultConfig = defaultConfig;
	}

	@Override
	protected void init() {
		this.optionsList = new ConfigOptionList(this, this.minecraft);
		this.addRenderableWidget(optionsList);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}
}
