package me.pandamods.pandalib.client.config.screen;

import com.mojang.blaze3d.platform.Window;
import me.pandamods.pandalib.client.config.screen.widgets.ConfigOptionWidget;
import me.pandamods.pandalib.client.config.screen.widgets.ConfigOptionList;
import me.pandamods.pandalib.client.screen.PandaLibScreen;
import me.pandamods.pandalib.config.Config;
import me.pandamods.pandalib.config.ConfigHolder;
import me.pandamods.pandalib.config.ConfigRegistry;
import me.pandamods.pandalib.utils.animation.interpolation.FloatInterpolator;
import me.pandamods.pandalib.utils.animation.interpolation.Vector2Interpolator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.joml.RoundingMode;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.awt.*;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ConfigScreen<T> extends PandaLibScreen {
	private final ConfigHolder<T> configHolder;
	private final ConfigOptionList configOptions;

	protected ConfigScreen(Screen parentScreen, ConfigHolder<T> configHolder) {
		super(parentScreen, GetConfigTitle(configHolder));
		this.configHolder = configHolder;

		this.configOptions = new ConfigOptionList(null, this, configHolder);
	}

	@Override
	protected void init() {
		this.configOptions.setBounds(0, 60, window.getGuiScaledWidth(), window.getGuiScaledHeight());
		this.addRenderableWidget(this.configOptions);

		super.init();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int i, int j, float f) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, i, j, f);
	}

	public static Screen Create(String modId, Screen parentScreen) {
		Map<Class<?>, ConfigHolder<?>> configs = ConfigRegistry.getConfigs(modId);
		if (configs.size() == 1) {
			return new ConfigScreen<>(parentScreen, configs.values().stream().findFirst().get());
		}
		return new ConfigListScreen(parentScreen, modId, configs.values());
	}

	public static Component GetConfigTitle(ConfigHolder<?> configHolder) {
		Config definition = configHolder.getDefinition();
		return Component.translatable("config." + definition.modId() + "." + definition.name() + ".title");
	}
}
