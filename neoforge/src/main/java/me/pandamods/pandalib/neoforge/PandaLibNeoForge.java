package me.pandamods.pandalib.neoforge;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.client.screen.config.menu.ConfigScreenProvider;
import me.pandamods.test.PandaLibTest;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(PandaLib.MOD_ID)
public class PandaLibNeoForge {
    public PandaLibNeoForge() {
		PandaLib.init();
		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (minecraft, screen) ->
				new ConfigScreenProvider<>(screen, PandaLibTest.CONFIG).get());
    }
}
