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

package me.pandamods.test.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.pandalib.client.render.ModelRenderer;
import me.pandamods.pandalib.client.render.TriangleRenderType;
import me.pandamods.pandalib.client.resource.model.Bone;
import me.pandamods.pandalib.client.resource.model.Model;
import me.pandamods.pandalib.client.resource.AssimpResources;
import me.pandamods.pandalib.utils.PrintUtils;
import me.pandamods.test.TestMod;
import me.pandamods.test.entities.SandWorm;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class SandWormRenderer extends EntityRenderer<SandWorm, SandWormRenderer.SandWormRenderState> {
	public SandWormRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	Matrix4f transform = new Matrix4f().identity();

	@Override
	public void render(SandWormRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(renderState, poseStack, bufferSource, packedLight);
//		Model model = AssimpResources.getModel(TestMod.resourceLocation("assimp/model/suzanne.fbx"));

		Model model = AssimpResources.getModel(TestMod.resourceLocation("assimp/model/bend_test.fbx"));
		Bone bone1 = model.getBones().get("Bone.001");
		Bone bone2 = model.getBones().get("Bone.002");

		transform.rotateX((float) Math.toRadians(1));
//		bone1.setLocalTransform(transform);
//		bone2.setLocalTransform(transform);

		ModelRenderer.render(model, poseStack, OverlayTexture.NO_OVERLAY, packedLight, s ->
				bufferSource.getBuffer(TriangleRenderType.getRenderType(RenderType.entityCutout(TestMod.resourceLocation("textures/entity/debug.png")))));
		ModelRenderer.renderArmature(model, poseStack, bufferSource);
	}

	@Override
	public @NotNull SandWormRenderState createRenderState() {
		return new SandWormRenderState();
	}

	public static class SandWormRenderState extends EntityRenderState {}
}
