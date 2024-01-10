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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ClientConfigHolder<T> extends ConfigHolder<T> {
	public static final EntityDataAccessor<CompoundTag> PLAYER_CLIENT_CONFIGS =
			SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);

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

	public T getClient(Player player) {
		CompoundTag compoundTag = player.getEntityData().get(PLAYER_CLIENT_CONFIGS);
		if (compoundTag.contains(resourceLocation().toString()))
			return GSON.fromJson(new String(compoundTag.getByteArray(resourceLocation().toString())), getConfigClass());
		return get();
	}

	public void setClientConfig(Player player, ResourceLocation resourceLocation, byte[] configBytes) {
		CompoundTag compoundTag = player.getEntityData().get(PLAYER_CLIENT_CONFIGS);
		compoundTag.remove(resourceLocation.toString());
		compoundTag.putByteArray(resourceLocation.toString(), configBytes);
		player.getEntityData().set(PLAYER_CLIENT_CONFIGS, compoundTag);
	}
}
