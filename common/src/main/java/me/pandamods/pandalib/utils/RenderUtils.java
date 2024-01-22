package me.pandamods.pandalib.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class RenderUtils {
	public static void renderBlock(PoseStack poseStack, BlockState blockState, BlockPos blockPos,
								   LevelAccessor level, VertexConsumer vertexConsumer, int overlay) {
		tesselate(level, blockState, blockPos, poseStack, vertexConsumer, level.getRandom(), overlay);
	}

	private static void tesselate(BlockAndTintGetter level, BlockState blockState, BlockPos blockPos, PoseStack poseStack,
						  VertexConsumer vertexConsumer, RandomSource random, int packedOverlay) {
		BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
		ModelBlockRenderer blockRenderer = blockRenderDispatcher.getModelRenderer();
		long seed = blockState.getSeed(blockPos);
		BakedModel model = blockRenderDispatcher.getBlockModel(blockState);

		BitSet bitSet = new BitSet(3);
		BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
		for (Direction direction : Direction.values()) {
			random.setSeed(seed);
			List<BakedQuad> quads = model.getQuads(blockState, direction, random);
			if (quads.isEmpty()) continue;
			mutableBlockPos.setWithOffset(blockPos, direction);
			int lightColor = LevelRenderer.getLightColor(level, blockState, blockPos);
			blockRenderer.renderModelFaceFlat(level, blockState, blockPos, lightColor, packedOverlay, false,
					poseStack, vertexConsumer, quads, bitSet);
		}
		random.setSeed(seed);
		List<BakedQuad> quads = model.getQuads(blockState, null, random);
		if (!quads.isEmpty()) {
			blockRenderer.renderModelFaceFlat(level, blockState, blockPos, -1, packedOverlay, true,
					poseStack, vertexConsumer, quads, bitSet);
		}
	}

	public static float getDeltaSeconds() {
		return Minecraft.getInstance().getDeltaFrameTime() / 20;
	}

	public static Set<ResourceLocation> getBlockTextures(BlockState blockState, Direction direction) {
		ModelManager manager = Minecraft.getInstance().getModelManager();
		BakedModel model = manager.getBlockModelShaper().getBlockModel(blockState);
		List<BakedQuad> quads = model.getQuads(blockState, direction, RandomSource.create());
		Set<ResourceLocation> textures = new HashSet<>();
		quads.forEach(bakedQuad -> textures.add(bakedQuad.getSprite().contents().name()));
		return textures;
	}
}
