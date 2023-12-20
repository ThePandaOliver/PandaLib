package me.pandalib.fabric;

import me.pandalib.PandaLib;
import net.fabricmc.api.ModInitializer;

public class PandaLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PandaLib.init();
    }
}
