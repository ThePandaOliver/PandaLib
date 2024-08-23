package me.pandamods.pandalib.forge;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.forge.client.PandaLibClientForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(PandaLib.MOD_ID)
public class PandaLibForge {
    public PandaLibForge() {
		PandaLib.init();

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> PandaLibClientForge::new);
    }
}
