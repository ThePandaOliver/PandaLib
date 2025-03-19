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

package dev.pandasystems.pandalib.client.resource;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import dev.pandasystems.pandalib.PandaLib;
import dev.pandasystems.pandalib.client.resource.animation.Animation;
import dev.pandasystems.pandalib.client.resource.loader.AnimationLoader;
import dev.pandasystems.pandalib.client.resource.model.Model;
import dev.pandasystems.pandalib.client.resource.loader.ModelLoader;
import dev.pandasystems.pandalib.registry.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.lwjgl.assimp.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AssimpResources implements IdentifiableResourceReloadListener {
	private static Map<ResourceLocation, Model> MODELS = new HashMap<>();
	private static Map<ResourceLocation, Animation> ANIMATIONS = new HashMap<>();

	public static Model getModel(ResourceLocation resourceLocation) {
		return MODELS.get(resourceLocation);
	}

	public static Animation getAnimation(ResourceLocation resourceLocation) {
		return ANIMATIONS.get(resourceLocation);
	}

	@Override
	public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager,
										  Executor backgroundExecutor, Executor gameExecutor) {
		List<AIScene> scenes = new ObjectArrayList<>();

		Map<ResourceLocation, Model> models = new HashMap<>();
		Map<ResourceLocation, Animation> animations = new HashMap<>();

		return CompletableFuture.allOf(loadAssimpScene(backgroundExecutor, resourceManager, scenes::add, models::put, animations::put))
				.thenCompose(preparationBarrier::wait)
				.thenAcceptAsync(unused -> {
					MODELS = models;
					ANIMATIONS = animations;
					scenes.forEach(Assimp::aiReleaseImport);
				}, gameExecutor);
	}

	private CompletableFuture<Void> loadAssimpScene(Executor executor, ResourceManager resourceManager,
													Consumer<AIScene> addScene,
													BiConsumer<ResourceLocation, Model> putModel,
													BiConsumer<ResourceLocation, Animation> putAnimation
	) {
		return CompletableFuture.supplyAsync(() -> resourceManager.listResources("assimp", resource -> true), executor)
				.thenApplyAsync(resources -> {
					Map<ResourceLocation, CompletableFuture<AIScene>> sceneTasks = new HashMap<>();

					for (ResourceLocation resourceLocation : resources.keySet()) {
						AIScene scene = loadAssimpScene(resourceManager, resourceLocation);
						addScene.accept(scene);

						if (scene != null) sceneTasks.put(resourceLocation, CompletableFuture.supplyAsync(() -> scene, executor));
					}
					return sceneTasks;
				}, executor).thenAcceptAsync(resource -> {
					for (Map.Entry<ResourceLocation, CompletableFuture<AIScene>> entry : resource.entrySet()) {
						ResourceLocation resourceLocation = entry.getKey();
						AIScene scene = entry.getValue().join();

						Model model = ModelLoader.loadFromScene(scene);
						putModel.accept(resourceLocation, model);

						for (int i = 0; i < scene.mNumAnimations(); i++) {
							AIAnimation aiAnimation = AIAnimation.create(scene.mAnimations().get(i));
							Animation animation = AnimationLoader.load(aiAnimation, model);

							ResourceLocation animLocation = ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), resourceLocation.getPath() + "/" + aiAnimation.mName().dataString());
							putAnimation.accept(animLocation, animation);
						}
					}
				}, executor);
	}

	private AIScene loadAssimpScene(ResourceManager resourceManager, ResourceLocation resourceLocation) {
		try (InputStream inputStream = resourceManager.getResourceOrThrow(resourceLocation).open()) {
			byte[] bytes = inputStream.readAllBytes();
			ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			return Assimp.aiImportFileFromMemory(buffer,
					Assimp.aiProcess_Triangulate | Assimp.aiProcess_PopulateArmatureData | Assimp.aiProcess_LimitBoneWeights, "");
		}
		catch (Exception e) {
			throw new RuntimeException(new FileNotFoundException(resourceLocation.toString()));
		}
	}

	@Override
	public ResourceLocation getResourceID() {
		return PandaLib.resourceLocation("assimp_resource_loader");
	}
}
