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

package me.pandamods.pandalib.client.animation.state;

import me.pandamods.pandalib.client.resource.model.Bone;
import me.pandamods.pandalib.client.resource.model.Model;
import org.joml.Matrix4f;

public abstract class State {
	private int ticks = 0;

	public void tick() {
		ticks++;
	}

	public int getTicks() {
		return ticks;
	}

	public float getTime(float partialTick) {
		return (ticks + partialTick) / 20;
	}

	public abstract void update(Model model, float partialTick);

	protected abstract Matrix4f getTransform(Bone bone, float partialTick);
}
