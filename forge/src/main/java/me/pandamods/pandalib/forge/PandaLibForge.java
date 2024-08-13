package me.pandamods.pandalib;

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
