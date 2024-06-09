package me.pandamods.pandalib.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.client.screen.config.menu.ConfigScreenProvider;
import me.pandamods.test.PandaLibTest;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.ConfigScreenHandler;

@Mod(PandaLib.MOD_ID)
public class PandaLibNeoForge {
    public PandaLibNeoForge() {
		PandaLib.init();
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
				new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) ->
						new ConfigScreenProvider<>(screen, PandaLibTest.CLIENT_CONFIG).get()));
    }
}
