package me.pandamods.pandalib.config.api.holders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.config.api.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;

public class CommonConfigHolder<T> extends ConfigHolder<T> {
	private T commonConfig;

	public CommonConfigHolder(Class<T> configClass, Config config) {
		super(configClass, config);
	}

	public void setCommonConfig(byte[] configBytes) {
		this.commonConfig = GSON.fromJson(new String(configBytes), getConfigClass());
	}

	@Override
	public T get() {
		if (Platform.getEnvironment().equals(Env.CLIENT))
			return this.commonConfig;
		return super.get();
	}
}
