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

package me.pandamods.pandalib.neoforge.event.events.common;

import me.pandamods.pandalib.event.events.common.BlockEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BlockEventsImpl {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
		if (event.getLevel() instanceof Level level) {
			if (BlockEvents.PLACE.invoker().place(level, event.getPos(), event.getState(), event.getEntity()).isFalse())
				event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onBlockDestroy(BlockEvent.BreakEvent event) {
		if (event.getPlayer() instanceof ServerPlayer serverPlayer && event.getLevel() instanceof Level level) {
			if (BlockEvents.DESTROY.invoker().destroy(level, event.getPos(), event.getState(), serverPlayer).isFalse())
				event.setCanceled(true);
		}
	}
}
