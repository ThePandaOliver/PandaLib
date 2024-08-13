package me.pandamods.pandalib.neoforge.platform;

import me.pandamods.pandalib.platform.ModLoader;
import net.neoforged.fml.ModList;

public class ModLoaderImpl implements ModLoader {
	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}
}
