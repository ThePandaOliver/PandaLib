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

package me.pandamods.pandalib.forge;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.event.events.NetworkingEvents;
import me.pandamods.pandalib.forge.client.PandaLibClientForge;
import me.pandamods.pandalib.forge.platform.NetworkHelperImpl;
import me.pandamods.pandalib.forge.platform.RegistrationHelperImpl;
import me.pandamods.pandalib.platform.Services;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PandaLib.MOD_ID)
public class PandaLibForge {
    public PandaLibForge(FMLJavaModLoadingContext context) {
	    IEventBus modEventBus = context.getModEventBus();
		new PandaLib();

	    if (Services.REGISTRATION instanceof RegistrationHelperImpl helper) {
		    modEventBus.addListener(helper::registerEvent);
		    MinecraftForge.EVENT_BUS.addListener(helper::addClientReloadListenerEvent);
		    MinecraftForge.EVENT_BUS.addListener(helper::addServerReloadListenerEvent);
	    }
	    NetworkingEvents.PACKET_PAYLOAD_REGISTRY.invoker().register(new NetworkHelperImpl());

	    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> new PandaLibClientForge(context));
    }
}
