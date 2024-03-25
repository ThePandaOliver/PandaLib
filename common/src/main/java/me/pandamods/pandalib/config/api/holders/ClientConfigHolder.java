package me.pandamods.pandalib.config.api.holders;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.ConfigData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

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
