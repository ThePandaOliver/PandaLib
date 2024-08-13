package me.pandamods.pandalib.fabric.platform;

import me.pandamods.pandalib.platform.ModLoader;
import me.pandamods.pandalib.platform.Platform;

import java.util.function.Supplier;

public class PlatformImpl implements Platform {
	private final ModLoader modLoader = new ModLoaderImpl();

	@Override
	public ModLoader getModLoader() {
		return this.modLoader;
	}
}
