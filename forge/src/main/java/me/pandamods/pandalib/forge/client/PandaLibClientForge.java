package me.pandamods.pandalib.forge.client;

import me.pandamods.pandalib.client.PandaLibClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

public class PandaLibClientForge {
    public PandaLibClientForge() {
		PandaLibClient.init();
    }
}
