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

package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.pandalib.client.resource.model.Bone;
import me.pandamods.pandalib.client.resource.model.Mesh;
import me.pandamods.pandalib.client.resource.model.Model;
import me.pandamods.pandalib.utils.CollectionsUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.function.Function;

public class ModelRenderer {
	public static void render(Model model, PoseStack poseStack, int overlayUV, int lightmapUV, Function<String, VertexConsumer> vertexConsumerProvider) {
		int previousTextureIndex = -1;
		VertexConsumer consumer = null;
		for (Mesh mesh : model.getMeshes()) {
			if (previousTextureIndex != mesh.textureIndex() || consumer == null) {
				consumer = vertexConsumerProvider.apply(model.getTextures().get(mesh.textureIndex()));
			}
			previousTextureIndex = mesh.textureIndex();
			if (consumer == null) continue;

			for (Mesh.Vertex vertex : mesh.vertices()) {
				Vector3f finalPos = new Vector3f();
				Vector3f finalNorm = new Vector3f();
				boolean hasWeights = false;

				for (Mesh.VertexWeight weight : vertex.weights()) {
					Bone bone = CollectionsUtils.getEntryByIndex(model.getBones().values(), weight.boneIndex());
					Vector3f tempPos = new Vector3f(vertex.x(), vertex.y(), vertex.z());
					Vector3f tempNorm = new Vector3f(vertex.normX(), vertex.normY(), vertex.normZ());

					Matrix4f boneTransform = bone.getGlobalTransform();
					Matrix4f inverseOffsetTransform = new Matrix4f(bone.getGlobalOffsetTransform()).invert();

					// Position
					tempPos.mulPosition(inverseOffsetTransform);
					tempPos.mulPosition(boneTransform);
					tempPos.mul(weight.weight());

					// Normal
					tempNorm.mulDirection(inverseOffsetTransform);
					tempNorm.mulDirection(boneTransform);
					tempNorm.mul(weight.weight());

					finalPos.add(tempPos);
					finalNorm.add(tempNorm);
					hasWeights = true;
				}

				if (!hasWeights) {
					finalPos.set(vertex.x(), vertex.y(), vertex.z());
					finalNorm.set(vertex.normX(), vertex.normY(), vertex.normZ());
				}

				consumer.addVertex(poseStack.last(), finalPos)
						.setColor(vertex.color().getRGB())
						.setUv(vertex.texU(), 1 - vertex.texV())
						.setOverlay(overlayUV)
						.setLight(lightmapUV)
						.setNormal(poseStack.last(), finalNorm.normalize());
			}
		}
	}

	public static void renderArmature(Model model, PoseStack poseStack, MultiBufferSource bufferSource) {
		VertexConsumer consumer = bufferSource.getBuffer(PLRenderType.debugLines(100));
		model.getBones().values().forEach(bone -> addJoint(bone, poseStack, consumer));
	}

	private static void addJoint(Bone bone, PoseStack poseStack, VertexConsumer consumer) {
		poseStack.pushPose();
		poseStack.mulPose(bone.getGlobalTransform());
		addJointBall(poseStack, consumer);
		poseStack.popPose();

		if (bone.getParent() != null) {
			Bone parent = bone.getParent();
			Vector3f rootPos = bone.getGlobalTransform().getTranslation(new Vector3f());
			Vector3f parentPos = parent.getGlobalTransform().getTranslation(new Vector3f());

			float length = rootPos.distance(parentPos);

			poseStack.pushPose();
			poseStack.mulPose(parent.getGlobalTransform());
			final float size = 0.1f;
			for (int i = 0; i < 4; i++) {
				Vector3f midPos = new Vector3f(size, length * 0.2f, size).rotateY((float) (i * Math.PI / 2));

				// Top
				consumer.addVertex(poseStack.last(), 0, length, 0)
						.setColor(1f, 1f, 1f, 1f);

				consumer.addVertex(poseStack.last(), midPos)
						.setColor(1f, 1f, 1f, 1f);

				// Bottom
				consumer.addVertex(poseStack.last(), midPos)
						.setColor(1f, 1f, 1f, 1f);

				consumer.addVertex(poseStack.last(), 0, 0, 0)
						.setColor(1f, 1f, 1f, 1f);
			}
			poseStack.popPose();
		}
	}

	private static void addJointBall(PoseStack poseStack, VertexConsumer consumer) {
		final float size = 0.1f;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 2; j++) {
				float x = (float) Math.cos((i + j) * Math.PI / 4) * size;
				float z = (float) Math.sin((i + j) * Math.PI / 4) * size;

				Vector3f pos = new Vector3f(x, 0, z);
				consumer.addVertex(poseStack.last(), pos)
						.setColor(0f, 0f, 1f, 1f);
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 2; j++) {
				float x = (float) Math.cos((i + j) * Math.PI / 4) * size;
				float y = (float) Math.sin((i + j) * Math.PI / 4) * size;

				Vector3f pos = new Vector3f(x, y, 0);
				consumer.addVertex(poseStack.last(), pos)
						.setColor(0f, 1f, 0f, 1f);
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 2; j++) {
				float y = (float) Math.cos((i + j) * Math.PI / 4) * size;
				float z = (float) Math.sin((i + j) * Math.PI / 4) * size;

				Vector3f pos = new Vector3f(0, y, z);
				consumer.addVertex(poseStack.last(), pos)
						.setColor(1f, 0f, 0f, 1f);
			}
		}
	}
}
