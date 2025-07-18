/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.test.entities

import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.level.block.Blocks

@Environment(EnvType.CLIENT)
class HelloEntityRenderer(context: EntityRendererProvider.Context) : EntityRenderer<HelloEntity, HelloEntityRenderState>(context) {
	override fun createRenderState(): HelloEntityRenderState {
		return HelloEntityRenderState()
	}

	override fun render(renderState: HelloEntityRenderState, poseStack: PoseStack, bufferSource: MultiBufferSource, packedLight: Int) {
		super.render(renderState, poseStack, bufferSource, packedLight)
		Minecraft.getInstance().blockRenderer.renderSingleBlock(Blocks.STONE.defaultBlockState(), poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY)
	}
}

class HelloEntityRenderState : EntityRenderState()