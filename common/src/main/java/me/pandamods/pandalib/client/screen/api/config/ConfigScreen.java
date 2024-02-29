package me.pandamods.pandalib.client.screen.api.config;

import me.pandamods.pandalib.client.screen.api.PLScreen;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;

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

		this.optionsList = new ConfigOptionList(this, configHolder, config, defaultConfig);
	}

	@Override
	protected void init() {
		this.addWidgetPanel(optionsList);
		super.init();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		guiGraphics.drawCenteredString(font, Component.translatable("config." + configHolder.getDefinition().modId() + ".title"),
				width / 2, 10, Color.white.getRGB());
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}
}
