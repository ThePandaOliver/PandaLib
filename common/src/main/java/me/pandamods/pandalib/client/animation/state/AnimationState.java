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

import me.pandamods.pandalib.client.resource.animation.Animation;
import me.pandamods.pandalib.client.resource.animation.Channel;
import me.pandamods.pandalib.client.resource.model.Bone;
import me.pandamods.pandalib.client.resource.model.Model;
import org.joml.Matrix4f;

public class AnimationState extends State {
	private final Animation animation;

	public AnimationState(Animation animation) {
		this.animation = animation;
	}

	@Override
	public void update(Model model, float partialTick) {
		for (Bone bone : model.getBones().values()) {
			bone.setLocalTransform(getTransform(bone, partialTick));
		}
	}

	@Override
	protected Matrix4f getTransform(Bone bone, float partialTick) {
		Channel channel = animation.getChannel(bone.getName());
		if (channel == null) return new Matrix4f();
		float time = getTime(partialTick);
		return channel.getMatrix(time);
	}
}
