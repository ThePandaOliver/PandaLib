package me.pandamods.pandalib.fabric;

import me.pandamods.pandalib.PandaLib;
import net.fabricmc.api.ModInitializer;

public class PandaLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PandaLib.init();
    }
}
