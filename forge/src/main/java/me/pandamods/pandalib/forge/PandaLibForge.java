package me.pandamods.pandalib.forge;

import dev.architectury.platform.forge.EventBuses;
import me.pandamods.pandalib.PandaLib;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PandaLib.MOD_ID)
public class PandaLibForge {
    public PandaLibForge() {
        EventBuses.registerModEventBus(PandaLib.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        PandaLib.init();

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> PandaLibForgeClient::clientInit);
    }
}
