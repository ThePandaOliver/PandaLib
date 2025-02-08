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

package me.pandamods.pandalib.client;

import me.pandamods.pandalib.client.resource.AssimpResources;
import me.pandamods.pandalib.core.client.event.EventHandlerClient;
import me.pandamods.pandalib.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.packs.PackType;

@Environment(EnvType.CLIENT)
public class PandaLibClient {
	private static PandaLibClient INSTANCE;
	
	public final AssimpResources assimpResources = new AssimpResources();
	
    public PandaLibClient() {
		INSTANCE = this;
		EventHandlerClient.init();

		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, assimpResources);
    }

	public static PandaLibClient getInstance() {
		return INSTANCE;
	}
}