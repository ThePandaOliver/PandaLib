package me.pandamods.pandalib.neoforge.client;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.client.PandaLibClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

#if MC_VER >= MC_1_21
@Mod(value = PandaLib.MOD_ID, dist = Dist.CLIENT)
#endif
public class PandaLibClientNeoForge {
    public PandaLibClientNeoForge() {
		PandaLibClient.init();
    }
}
