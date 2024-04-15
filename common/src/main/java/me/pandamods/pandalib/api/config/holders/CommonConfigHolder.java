package me.pandamods.pandalib.api.config.holders;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.api.config.Config;
import me.pandamods.pandalib.api.config.ConfigData;

public class CommonConfigHolder<T extends ConfigData> extends ConfigHolder<T> {
	private T commonConfig;

	public CommonConfigHolder(Class<T> configClass, Config config) {
		super(configClass, config);
	}

	@SuppressWarnings("unchecked")
	public <C extends ConfigData> void setCommonConfig(C config) {
		this.commonConfig = (T) config;
	}

	@Override
	public T get() {
		if (Platform.getEnvironment().equals(Env.CLIENT) && this.commonConfig != null)
			return this.commonConfig;
		return super.get();
	}
}
