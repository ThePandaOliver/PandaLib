package me.pandamods.pandalib.neoforge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.neoforge.client.PandaLibClientNeoForge;
import me.pandamods.pandalib.platform.Services;
import net.neoforged.fml.common.Mod;

@Mod(PandaLib.MOD_ID)
public class PandaLibNeoForge {
    public PandaLibNeoForge() {
		PandaLib.init();

		#if MC_VER < MC_1_21
		if (Platform.getEnvironment().equals(Env.CLIENT))
			new PandaLibClientNeoForge();
		#endif
    }
}
