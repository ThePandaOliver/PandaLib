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

import me.pandamods.pandalib.config.ConfigData;
import me.pandamods.pandalib.config.PandaLibConfig;
import me.pandamods.pandalib.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.config.holders.ConfigHolder;
import me.pandamods.pandalib.extensions.PlayerExtension;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerExtension {
	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Deprecated(forRemoval = true, since = "0.4")
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConfigData> void pandaLib$setConfig(T config) {
		ConfigHolder<T> holder = (ConfigHolder<T>) PandaLibConfig.getConfig(config.getClass());
		if (holder instanceof ClientConfigHolder<T> clientHolder) {
			clientHolder.putConfig((Player)(Object) this, config);
		}
	}

	@Deprecated(forRemoval = true, since = "0.4")
	@Override
	public <T extends ConfigData> T pandaLib$getConfig(Class<T> configClass) {
		ConfigHolder<T> holder = PandaLibConfig.getConfig(configClass);
		if (holder instanceof ClientConfigHolder<T> clientHolder) {
			return clientHolder.getConfig((Player)(Object) this);
		}
		return PlayerExtension.super.pandaLib$getConfig(configClass);
	}
}
