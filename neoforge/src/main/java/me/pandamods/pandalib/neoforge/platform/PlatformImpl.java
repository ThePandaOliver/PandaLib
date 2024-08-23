package me.pandamods.pandalib.neoforge.platform;

import me.pandamods.pandalib.platform.ModLoader;
import me.pandamods.pandalib.platform.Platform;

public class PlatformImpl implements Platform {
	private final ModLoader modLoader = new ModLoaderImpl();

	@Override
	public ModLoader getModLoader() {
		return this.modLoader;
	}
}
