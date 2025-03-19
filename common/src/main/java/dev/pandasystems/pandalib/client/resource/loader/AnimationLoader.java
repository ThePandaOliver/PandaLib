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

package dev.pandasystems.pandalib.client.resource.loader;

import dev.pandasystems.pandalib.client.resource.animation.Animation;
import dev.pandasystems.pandalib.client.resource.animation.Channel;
import dev.pandasystems.pandalib.client.resource.model.Bone;
import dev.pandasystems.pandalib.client.resource.model.Model;
import dev.pandasystems.pandalib.utils.AssimpUtils;
import org.joml.*;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuatKey;
import org.lwjgl.assimp.AIVectorKey;

import java.util.ArrayList;
import java.util.List;

public class AnimationLoader {
	public static Animation load(AIAnimation aiAnimation, Model model) {
		List<Channel> channels = new ArrayList<>();
		float durationInSeconds = (float) (aiAnimation.mDuration() / aiAnimation.mTicksPerSecond());

		for (int i = 0; i < aiAnimation.mNumChannels(); i++) {
			AINodeAnim aiNodeAnim = AINodeAnim.create(aiAnimation.mChannels().get(i));

			Bone bone = model.getBones().get(aiNodeAnim.mNodeName().dataString());
			Matrix4f boneOffset = new Matrix4f(bone.getGlobalOffsetTransform()).invert();

			List<Channel.Key<Vector3fc>> positionKeys = new ArrayList<>();
			List<Channel.Key<Quaternionfc>> rotationKeys = new ArrayList<>();
			List<Channel.Key<Vector3fc>> scalingKeys = new ArrayList<>();

			for (int j = 0; j < aiNodeAnim.mNumPositionKeys(); j++) {
				AIVectorKey aiKey = aiNodeAnim.mPositionKeys().get(j);
				float timeInSeconds = (float) (aiKey.mTime() / aiAnimation.mTicksPerSecond());

				Vector3f position = AssimpUtils.toVector3f(aiKey.mValue());
				position.sub(boneOffset.getTranslation(new Vector3f()));

				positionKeys.add(new Channel.Key<>(timeInSeconds, position));
			}

			for (int j = 0; j < aiNodeAnim.mNumRotationKeys(); j++) {
				AIQuatKey aiKey = aiNodeAnim.mRotationKeys().get(j);
				float timeInSeconds = (float) (aiKey.mTime() / aiAnimation.mTicksPerSecond());

				Quaternionf rotation = AssimpUtils.toQuaternionf(aiKey.mValue());
				rotation.mul(boneOffset.getNormalizedRotation(new Quaternionf()));

				rotationKeys.add(new Channel.Key<>(timeInSeconds, rotation));
			}

			for (int j = 0; j < aiNodeAnim.mNumScalingKeys(); j++) {
				AIVectorKey aiKey = aiNodeAnim.mScalingKeys().get(j);
				float timeInSeconds = (float) (aiKey.mTime() / aiAnimation.mTicksPerSecond());

				Vector3f scale = AssimpUtils.toVector3f(aiKey.mValue());
				scale.mul(boneOffset.getScale(new Vector3f()));

				scalingKeys.add(new Channel.Key<>(timeInSeconds, scale));
			}

			channels.add(new Channel(aiNodeAnim.mNodeName().dataString(), positionKeys, rotationKeys, scalingKeys));
		}

		return new Animation(channels, durationInSeconds);
	}
}
