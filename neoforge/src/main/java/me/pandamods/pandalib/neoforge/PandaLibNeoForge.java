package me.pandamods.pandalib.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import me.pandamods.pandalib.PandaLib;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(PandaLib.MOD_ID)
public class PandaLibNeoForge {
    public PandaLibNeoForge() {
//		EventBuses.registerModEventBus(PandaLib.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		PandaLib.init();
		if (FMLEnvironment.dist.isClient()) {
			PandaLibNeoForgeClient.clientInit();
		}
    }
}
