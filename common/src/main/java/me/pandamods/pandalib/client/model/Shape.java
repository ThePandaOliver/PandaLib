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

package me.pandamods.pandalib.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class Shape {
	private final Vector3f position;
	private final Vector3f rotation;
	private final Vector3f size;
	private final Vector3f pivot;

	private final Map<String, Face> faces;

	public Shape(Vector3f position, Vector3f rotation, Vector3f size, Vector3f pivot, Map<String, Face> faces) {
		this.position = position;
		this.rotation = rotation;
		this.size = size;
		this.pivot = pivot;
		this.faces = faces;
	}

	public void render(Model model, PoseStack stack, Function<String, VertexConsumer> getVertexConsumer) {
		stack.pushPose();
		stack.translate(this.position.x, this.position.y, this.position.z);
		stack.rotateAround(new Quaternionf().identity().rotationZYX(rotation.z(), rotation.y(), rotation.x()), this.pivot.x, this.pivot.y, this.pivot.z);

		// Top
		if (this.faces.containsKey("top")) {
			Face face = this.faces.get("top");
			VertexConsumer consumer = getVertexConsumer.apply(model.getTextures().get(face.textureIndex));

			consumer.addVertex(stack.last(), 0, size.y, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 1, 0);

			consumer.addVertex(stack.last(), size.x, size.y, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 1, 0);

			consumer.addVertex(stack.last(), size.x, size.y, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 1, 0);

			consumer.addVertex(stack.last(), 0, size.y, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 1, 0);
		}

		// Bottom
		if (this.faces.containsKey("bottom")) {
			Face face = this.faces.get("bottom");
			VertexConsumer consumer = getVertexConsumer.apply(model.getTextures().get(face.textureIndex));

			consumer.addVertex(stack.last(), 0, 0, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, -1, 0);

			consumer.addVertex(stack.last(), size.x, 0, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, -1, 0);

			consumer.addVertex(stack.last(), size.x, 0, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, -1, 0);

			consumer.addVertex(stack.last(), 0, 0, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, -1, 0);
		}

		// North
		if (this.faces.containsKey("north")) {
			Face face = this.faces.get("north");
			VertexConsumer consumer = getVertexConsumer.apply(model.getTextures().get(face.textureIndex));

			consumer.addVertex(stack.last(), 0, 0, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 0, 1);

			consumer.addVertex(stack.last(), size.x, 0, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 0, 1);

			consumer.addVertex(stack.last(), size.x, size.y, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 0, 1);

			consumer.addVertex(stack.last(), 0, size.y, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 0, 1);
		}

		// South
		if (this.faces.containsKey("south")) {
			Face face = this.faces.get("south");
			VertexConsumer consumer = getVertexConsumer.apply(model.getTextures().get(face.textureIndex));

			consumer.addVertex(stack.last(), 0, 0, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 0, -1);

			consumer.addVertex(stack.last(), size.x, 0, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 0, -1);

			consumer.addVertex(stack.last(), size.x, size.y, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 0, -1);

			consumer.addVertex(stack.last(), 0, size.y, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 0, 0, -1);
		}

		// East
		if (this.faces.containsKey("east")) {
			Face face = this.faces.get("east");
			VertexConsumer consumer = getVertexConsumer.apply(model.getTextures().get(face.textureIndex));

			consumer.addVertex(stack.last(), 0, 0, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), -1, 0, 0);

			consumer.addVertex(stack.last(), 0, 0, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), -1, 0, 0);

			consumer.addVertex(stack.last(), 0, size.y, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), -1, 0, 0);

			consumer.addVertex(stack.last(), 0, size.y, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), -1, 0, 0);
		}

		// West
		if (this.faces.containsKey("west")) {
			Face face = this.faces.get("west");
			VertexConsumer consumer = getVertexConsumer.apply(model.getTextures().get(face.textureIndex));

			consumer.addVertex(stack.last(), size.x, 0, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 1, 0, 0);

			consumer.addVertex(stack.last(), size.x, 0, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY + face.uvHeight)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 1, 0, 0);

			consumer.addVertex(stack.last(), size.x, size.y, size.z)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX + face.uvWidth, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 1, 0, 0);

			consumer.addVertex(stack.last(), size.x, size.y, 0)
					.setColor(255, 255, 255, 255)
					.setUv(face.uvX, face.uvY)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(15728880)
					.setNormal(stack.last(), 1, 0, 0);
		}

		stack.popPose();
	}

	public record Face(String name, int rotationIndex, int textureIndex, float uvX, float uvY, float uvWidth, float uvHeight) { }
}
