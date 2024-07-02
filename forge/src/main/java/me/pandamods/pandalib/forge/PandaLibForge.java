package me.pandamods.pandalib.forge;

import me.pandamods.pandalib.PandaLib;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(PandaLib.MOD_ID)
public class PandaLibForge {
    public PandaLibForge() {
		PandaLib.init();
    }
}
