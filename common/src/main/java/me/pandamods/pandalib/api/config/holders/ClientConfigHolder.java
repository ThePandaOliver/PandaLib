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

package me.pandamods.pandalib.api.config.holders;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.api.config.Config;
import me.pandamods.pandalib.api.config.ConfigData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientConfigHolder<T extends ConfigData> extends ConfigHolder<T> {
	private final Map<UUID, T> configs = new HashMap<>();

	public ClientConfigHolder(Class<T> configClass, Config config) {
		super(configClass, config);
	}

	@Override
	public void save() {
		if (Platform.getEnvironment().equals(Env.CLIENT)) {
			super.save();
		} else
			this.logger.warn("Client config '{}' can't be saved on server", this.getDefinition().name());
	}

	@Override
	public boolean load() {
		if (Platform.getEnvironment().equals(Env.CLIENT)) {
			return super.load();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public <C extends ConfigData> void putConfig(Player player, C config) {
		configs.put(player.getUUID(), (T) config);
	}

	public T getConfig(Player player) {
		if (configs.containsKey(player.getUUID()))
			return configs.get(player.getUUID());
		return this.get();
	}
}
