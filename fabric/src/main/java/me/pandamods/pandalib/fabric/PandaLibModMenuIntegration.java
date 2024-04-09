package me.pandamods.pandalib.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.pandamods.pandalib.api.client.screen.config.ConfigCategory;
import me.pandamods.pandalib.api.client.screen.config.ConfigMenu;
import me.pandamods.pandalib.api.client.screen.config.option.StringOption;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import me.pandamods.test.config.TestClientConfig;
import net.minecraft.network.chat.Component;

public class PandaLibModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		ConfigHolder<TestClientConfig> configHolder = PandaLibConfig.getConfig(TestClientConfig.class);
		return configHolder.buildScreen()::Build;
	}
}
