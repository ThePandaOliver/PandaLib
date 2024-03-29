package me.pandamods.pandalib.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.pandamods.pandalib.api.client.screen.config.ConfigCategory;
import me.pandamods.pandalib.api.client.screen.config.ConfigMenu;
import me.pandamods.pandalib.api.client.screen.config.option.StringOption;
import me.pandamods.test.config.TestClientConfig;
import net.minecraft.network.chat.Component;

public class PandaLibModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		ConfigMenu.Builder<TestClientConfig> menuBuilder = new ConfigMenu.Builder<>(TestClientConfig.class);

		ConfigCategory.Builder<TestClientConfig> main = ConfigCategory.builder(Component.literal("main"));
		main.addOption(new StringOption<>(Component.literal("testString1"), config -> "", (config, string) -> {}, config -> ""));
		main.addOption(new StringOption<>(Component.literal("testString2"), config -> "", (config, string) -> {}, config -> ""));
		main.addOption(new StringOption<>(Component.literal("testString3"), config -> "", (config, string) -> {}, config -> ""));

		ConfigCategory.Builder<TestClientConfig> test1 = ConfigCategory.builder(Component.literal("test1"));
		test1.addOption(new StringOption<>(Component.literal("testString1"), config -> "", (config, string) -> {}, config -> ""));
		test1.addOption(new StringOption<>(Component.literal("testString2"), config -> "", (config, string) -> {}, config -> ""));
		test1.addOption(new StringOption<>(Component.literal("testString3"), config -> "", (config, string) -> {}, config -> ""));

		ConfigCategory.Builder<TestClientConfig> test2 = ConfigCategory.builder(Component.literal("test2"));
		test2.addOption(new StringOption<>(Component.literal("testString1"), config -> "", (config, string) -> {}, config -> ""));
		test2.addOption(new StringOption<>(Component.literal("testString2"), config -> "", (config, string) -> {}, config -> ""));
		test2.addOption(new StringOption<>(Component.literal("testString3"), config -> "", (config, string) -> {}, config -> ""));

		return menuBuilder
				.registerCategory(main.build())
				.registerCategory(test1.build())
				.registerCategory(test2.build())
				::Build;
	}
}
