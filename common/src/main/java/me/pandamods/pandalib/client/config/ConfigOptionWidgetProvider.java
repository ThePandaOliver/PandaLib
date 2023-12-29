package me.pandamods.pandalib.client.config;

import me.pandamods.pandalib.client.config.screen.widgets.ConfigOptionWidget;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.config.ConfigHolder;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public interface ConfigOptionWidgetProvider {
	ConfigOptionWidget create(@Nullable Widget parentWidget, Screen screen, Field field, ConfigHolder<?> configHolder);
}
