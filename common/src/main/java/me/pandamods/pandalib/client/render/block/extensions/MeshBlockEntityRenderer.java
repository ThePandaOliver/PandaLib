package me.pandamods.pandalib.client.render.block.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.pandalib.client.render.MeshRenderer;
import me.pandamods.pandalib.resources.MeshRecord;
import me.pandamods.pandalib.cache.ObjectCache;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Map;
import java.util.SortedSet;

@Environment(EnvType.CLIENT)
public abstract class MeshBlockEntityRenderer<T extends BlockEntity & MeshAnimatable, M extends MeshModel<T>>
		implements MeshRenderer<T, M>, BlockEntityRenderer<T> {
	private final M model;

	public MeshBlockEntityRenderer(BlockEntityRendererProvider.Context context, M model) {
		this.model = model;
	}


	@Override
	public void render(T blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		stack.pushPose();
		translateBlock(blockEntity.getBlockState(), stack);
		this.renderRig(blockEntity, model, stack, buffer, packedLight, packedOverlay);
		stack.popPose();
	}

	@Override
	public void renderObject(MeshRecord.Object object, T base, M model, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay,
							 Color color,
							 Map<Integer, ObjectCache.VertexCache> cachedVertices, Map<Integer, ObjectCache.FaceCache> cachedFaces,
							 Map<Integer, ObjectCache.VertexCache> newCachedVertices, Map<Integer, ObjectCache.FaceCache> newCachedFaces) {
		MeshRenderer.super.renderObject(object, base, model, stack, buffer, packedLight, packedOverlay, color,
				cachedVertices, cachedFaces, newCachedVertices, newCachedFaces);
	}
}
