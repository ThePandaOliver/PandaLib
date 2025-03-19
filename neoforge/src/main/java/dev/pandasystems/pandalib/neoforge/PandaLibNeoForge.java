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

package dev.pandasystems.pandalib.neoforge;

import dev.pandasystems.pandalib.PandaLib;
import dev.pandasystems.pandalib.neoforge.platform.NetworkHelperImpl;
import dev.pandasystems.pandalib.neoforge.platform.RegistrationHelperImpl;
import dev.pandasystems.pandalib.platform.Services;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(PandaLib.MOD_ID)
public class PandaLibNeoForge {
    public PandaLibNeoForge(IEventBus eventBus) {
		new PandaLib();
		
		eventBus.addListener(NetworkHelperImpl::registerPackets);
		if (Services.REGISTRATION instanceof RegistrationHelperImpl helper) {
			eventBus.addListener(helper::registerEvent);
			eventBus.addListener(helper::registerNewRegistryEvent);
			NeoForge.EVENT_BUS.addListener(helper::addServerReloadListenerEvent);
		}
    }
}
