package me.pandamods.pandalib.fabric.platform;

import me.pandamods.pandalib.platform.ModLoader;
import net.fabricmc.loader.api.FabricLoader;

public class ModLoaderImpl implements ModLoader {
	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}
}
