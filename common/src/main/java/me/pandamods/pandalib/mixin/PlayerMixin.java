package me.pandamods.pandalib.mixin;

import com.mojang.authlib.GameProfile;
import me.pandamods.pandalib.config.api.ConfigData;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;
import me.pandamods.pandalib.impl.PlayerImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerImpl {
	@Unique
	private static final EntityDataAccessor<CompoundTag> PLAYER_CLIENT_CONFIGS =
			SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(PLAYER_CLIENT_CONFIGS, new CompoundTag());
		super.defineSynchedData();
	}

	@Override
	public void pandaLib$setConfig(ResourceLocation resourceLocation, byte[] configBytes) {
		CompoundTag compoundTag = this.getEntityData().get(PLAYER_CLIENT_CONFIGS);
		compoundTag.putByteArray(resourceLocation.toString(), configBytes);
		this.getEntityData().set(PLAYER_CLIENT_CONFIGS, compoundTag);
	}

	@Override
	public <T extends ConfigData> T pandaLib$getConfig(ConfigHolder<T> holder) {
		CompoundTag compoundTag = this.getEntityData().get(PLAYER_CLIENT_CONFIGS);
		if (compoundTag.contains(holder.resourceLocation().toString()))
			return holder.getGson().fromJson(new String(compoundTag.getByteArray(holder.resourceLocation().toString())), holder.getConfigClass());
		return holder.get();
	}
}
