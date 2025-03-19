/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.config.holders;

import dev.pandasystems.pandalib.config.Config;
import dev.pandasystems.pandalib.config.ConfigData;
import dev.pandasystems.pandalib.platform.Services;
import dev.pandasystems.pandalib.utils.Env;
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
		if (Services.GAME.getEnvironment().equals(Env.CLIENT)) {
			super.save();
		} else
			this.logger.warn("Client config '{}' can't be saved on server", this.getDefinition().name());
	}

	@Override
	public boolean load() {
		if (Services.GAME.getEnvironment().equals(Env.CLIENT)) {
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
