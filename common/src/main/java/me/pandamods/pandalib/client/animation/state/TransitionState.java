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
import me.pandamods.pandalib.utils.MathUtils;
import org.joml.Matrix4f;

public class TransitionState extends State {
	private final float duration;
	private State from;
	private State to;

	public TransitionState(State from, State to, float duration) {
		this.from = from;
		this.to = to;
		this.duration = duration;
	}

	@Override
	public void update(Model model, float partialTicks) {
		for (Bone bone : model.getBones().values()) {
			bone.setLocalTransform(getTransform(bone, partialTicks));
		}
	}

	@Override
	protected Matrix4f getTransform(Bone bone, float partialTick) {
		Matrix4f fromMatrix = from.getTransform(bone, partialTick);
		Matrix4f toMatrix = to.getTransform(bone, partialTick);
		return MathUtils.lerpMatrix(fromMatrix, toMatrix, this.getTime(partialTick));
	}
}
