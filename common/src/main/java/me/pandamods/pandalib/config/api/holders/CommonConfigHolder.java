package me.pandamods.pandalib.config.api.holders;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.ConfigData;

public class CommonConfigHolder<T extends ConfigData> extends ConfigHolder<T> {
	private T commonConfig;

	public CommonConfigHolder(Class<T> configClass, Config config) {
		super(configClass, config);
	}

	public void setCommonConfig(byte[] configBytes) {
		this.commonConfig = this.getGson().fromJson(new String(configBytes), getConfigClass());
	}

	@Override
	public T get() {
		if (Platform.getEnvironment().equals(Env.CLIENT) && this.commonConfig != null)
			return this.commonConfig;
		return super.get();
	}
}
