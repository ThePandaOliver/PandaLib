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

package me.pandamods.pandalib.event.events.common;

import me.pandamods.pandalib.event.Event;
import me.pandamods.pandalib.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BlockEvents {
	Event<Place> PLACE = EventFactory.createEvent();
	Event<Destroy> DESTROY = EventFactory.createEvent();

	interface Place {
		boolean place(Level level, BlockPos pos, BlockState state, @Nullable Entity placer);
	}

	interface Destroy {
		boolean destroy(Level level, BlockPos pos, BlockState state, ServerPlayer destroyer);
	}
}
