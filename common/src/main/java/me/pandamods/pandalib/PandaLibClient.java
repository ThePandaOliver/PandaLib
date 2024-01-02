package me.pandamods.pandalib;

import me.pandamods.pandalib.client.config.ConfigGuiRegistry;
import me.pandamods.pandalib.client.config.screen.widgets.ConfigOptionWidget;
import me.pandamods.pandalib.client.config.screen.widgets.options.BooleanOptionWidget;
import me.pandamods.pandalib.client.config.screen.widgets.options.StringOptionWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PandaLibClient {
	public static void init() {
		ConfigGuiRegistry.registerByClass(String.class, StringOptionWidget::new);
		ConfigGuiRegistry.registerByClass(boolean.class, BooleanOptionWidget::new);
		ConfigGuiRegistry.registerByClass(Boolean.class, BooleanOptionWidget::new);
	}
}
