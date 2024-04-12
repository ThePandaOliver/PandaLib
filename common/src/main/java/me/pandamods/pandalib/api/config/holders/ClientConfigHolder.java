package me.pandamods.pandalib.api.config.holders;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.api.annotation.Config;
import me.pandamods.pandalib.api.config.ConfigData;

public class ClientConfigHolder<T extends ConfigData> extends ConfigHolder<T> {
	public ClientConfigHolder(Class<T> configClass, Config config) {
		super(configClass, config);
	}

	@Override
	public void save() {
		if (Platform.getEnvironment().equals(Env.CLIENT)) {
			super.save();
		}
	}

	@Override
	public boolean load() {
		if (Platform.getEnvironment().equals(Env.CLIENT)) {
			return super.load();
		}
		return false;
	}
}
