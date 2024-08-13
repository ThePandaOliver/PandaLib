package me.pandamods.pandalib.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
public class PandaLibClientForge {
    public PandaLibClientForge() {
		PandaLibClient.init();
    }
}
