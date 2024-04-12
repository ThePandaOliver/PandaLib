package me.pandamods.pandalib.api.config.holders;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.api.annotation.Config;
import me.pandamods.pandalib.api.config.ConfigData;

public class CommonConfigHolder<T extends ConfigData> extends ConfigHolder<T> {
	private T commonConfig;

	public CommonConfigHolder(Class<T> configClass, Config config) {
		super(configClass, config);
	}

	public void setCommonConfig(String configJson) {
		this.commonConfig = this.getGson().fromJson(configJson, getConfigClass());
	}

	@Override
	public T get() {
		if (Platform.getEnvironment().equals(Env.CLIENT) && this.commonConfig != null)
			return this.commonConfig;
		return super.get();
	}
}
