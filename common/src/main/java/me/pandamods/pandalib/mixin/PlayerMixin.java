/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.mixin;

import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import me.pandamods.pandalib.core.extensions.PlayerExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerExtension {
	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConfigData> void pandaLib$setConfig(T config) {
		ConfigHolder<T> holder = (ConfigHolder<T>) PandaLibConfig.getConfig(config.getClass());
		if (holder instanceof ClientConfigHolder<T> clientHolder) {
			clientHolder.putConfig((Player)(Object) this, config);
		}
	}

	@Override
	public <T extends ConfigData> T pandaLib$getConfig(Class<T> configClass) {
		ConfigHolder<T> holder = PandaLibConfig.getConfig(configClass);
		if (holder instanceof ClientConfigHolder<T> clientHolder) {
			return clientHolder.getConfig((Player)(Object) this);
		}
		return PlayerExtension.super.pandaLib$getConfig(configClass);
	}
}
