package me.pandamods.pandalib.forge;

import dev.architectury.platform.forge.EventBuses;
import me.pandamods.pandalib.PandaLib;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PandaLib.MOD_ID)
public class PandaLibForge {
    public PandaLibForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(PandaLib.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        PandaLib.init();
    }
}
