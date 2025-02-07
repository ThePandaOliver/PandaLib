/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.resource;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.mojang.logging.LogUtils;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.client.PandaLibClient;
import me.pandamods.pandalib.client.model.Node;
import me.pandamods.pandalib.client.model.Shape;
import me.pandamods.pandalib.registry.IdentifiableResourceReloadListener;
import me.pandamods.pandalib.client.model.Model;
import me.pandamods.pandalib.utils.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import org.joml.Vector3f;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ModelManager implements IdentifiableResourceReloadListener {
	public static final ResourceLocation ID = PandaLib.resourceLocation("model_manager");
	
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = new GsonBuilder().create();
	
	private Map<ResourceLocation, Model> models = ImmutableMap.of();

	public static Model getModel(ResourceLocation resourceLocation) {
		return PandaLibClient.getInstance().modelManager.models.get(resourceLocation);
	}

	@Override
	public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, Executor backgroundExecutor, Executor gameExecutor) {
		Map<ResourceLocation, Model> models = Maps.newHashMap();
		
		return preparationBarrier.wait(Unit.INSTANCE)
				.thenAcceptAsync(unused -> {
					Set<Map.Entry<ResourceLocation, Resource>> entries = resourceManager.listResources("pandalib/model", 
							resource -> resource.getPath().endsWith(".plm")).entrySet();
					for (Map.Entry<ResourceLocation, Resource> entry : entries) {
						ResourceLocation resourceLocation = entry.getKey();
						Resource resource = entry.getValue();
						
						JsonObject modelJson;
						try (BufferedReader reader = resource.openAsReader()) {
							modelJson = GSON.fromJson(reader, JsonObject.class);
							
							if (modelJson == null) {
								LOGGER.error("Failed to load model {}", resourceLocation);
							}
						} catch (IOException e) {
							LOGGER.error("Failed to load model {}", resourceLocation, e);
							continue;
						}
						assert modelJson != null;
						
						if (!modelJson.has("plm_version" ))
							throw new RuntimeException("Unsupported PLM format " + resourceLocation);
						else if (!modelJson.getAsJsonPrimitive("plm_version").getAsString().equalsIgnoreCase(Constants.SUPPORTED_PLM_VERSION))
							throw new RuntimeException(String.format("Unsupported PLM version %s, expected [%s] but found [%s]",
									resourceLocation, Constants.SUPPORTED_PLM_VERSION,  modelJson.getAsJsonPrimitive("plm_version").getAsString()));
						
						models.put(resourceLocation, parseModel(resourceLocation, modelJson));
					}
					this.models = ImmutableMap.copyOf(models);
				}, gameExecutor);
	}
	
	private Model parseModel(ResourceLocation resourceLocation, JsonObject modelJson) {
		// UV size

		JsonArray uvSize = modelJson.getAsJsonArray("uv_size");
		if (uvSize.size() != 2)
			throw new RuntimeException(String.format("PLM %s: Invalid 'uv_size' array length, expected 2 but found %d", resourceLocation, uvSize.size()));

		JsonElement uvSizeX = uvSize.get(0);
		JsonElement uvSizeY = uvSize.get(1);

		if (!(uvSizeX.isJsonPrimitive() && uvSizeX.getAsJsonPrimitive().isNumber()) ||
				!(uvSizeY.isJsonPrimitive() && uvSizeY.getAsJsonPrimitive().isNumber()))
			throw new RuntimeException(String.format("PLM %s: 'uv_size' contains invalid element types", resourceLocation));

		int textureWidth = uvSize.get(0).getAsInt();
		int textureHeight = uvSize.get(1).getAsInt();

		// Textures

		JsonArray texturesArray = modelJson.getAsJsonArray("textures");
		List<String> textures = new ArrayList<>();
		for (JsonElement jsonElement : texturesArray) {
			if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
				textures.add(jsonElement.getAsString());
			} else {
				throw new RuntimeException(String.format("PLM %s: 'textures' contains invalid element types", resourceLocation));
			}
		}

		// Nodes

		Map<String, Node> nodes = new HashMap<>();
		Node rootNode = new Node("root", null, null);

		JsonArray nodesArray = modelJson.getAsJsonArray("nodes");
		nodesArray.forEach(jsonElement -> {
			if (!jsonElement.isJsonObject())
				throw new RuntimeException(String.format("PLM %s: 'nodes' contains invalid element types", resourceLocation));
			parseNode(resourceLocation, jsonElement.getAsJsonObject(), nodes);
		});

		return new Model(textureWidth, textureHeight, textures, rootNode, nodes.values().stream().toList());
	}

	private Node parseNode(ResourceLocation resourceLocation, JsonObject nodeJson, Map<String, Node> nodes) {
		// Name

		JsonElement nameElement = nodeJson.get("name");
		if (!(nameElement != null && nameElement.isJsonPrimitive() && nameElement.getAsJsonPrimitive().isString()))
			throw new RuntimeException(String.format("PLM %s: 'name' invalid element type", resourceLocation));
		String name = nodeJson.getAsJsonPrimitive("name").getAsString();

		// Parent

		JsonElement parentElement = nodeJson.get("parent");
		String parentName;
		if (parentElement == null) {
			parentName = "root";
		} else if (!(parentElement.isJsonPrimitive() && parentElement.getAsJsonPrimitive().isString())) {
			throw new RuntimeException(String.format("PLM %s: 'parent' invalid element type", resourceLocation));
		} else {
			parentName = nodeJson.getAsJsonPrimitive("parent").getAsString();
		}

		// Shape

		JsonElement shapeElement = nodeJson.get("shape");
		Shape shape = parseShape(resourceLocation, shapeElement);

		Node node = new Node(name, nodes.get(parentName), shape);
		nodes.put(name, node);
		return node;
	}

	private Shape parseShape(ResourceLocation resourceLocation, JsonElement shapeElement) {
		if (shapeElement == null)
			return null;

		if (!(shapeElement.isJsonObject()))
			throw new RuntimeException(String.format("PLM %s: 'shape' invalid element type", resourceLocation));
		JsonObject shapeObject = shapeElement.getAsJsonObject();

		JsonElement positionElement = shapeObject.get("position");
		if (!(positionElement != null && positionElement.isJsonArray()))
			throw new RuntimeException(String.format("PLM %s: 'shape.position' invalid element type", resourceLocation));
		JsonArray positionArray = positionElement.getAsJsonArray();
		if (positionArray.size() != 3)
			throw new RuntimeException(String.format("PLM %s: 'shape.position' array length, expected 3 but found %d", resourceLocation, positionArray.size()));
		Vector3f position = new Vector3f(positionArray.get(0).getAsFloat(), positionArray.get(1).getAsFloat(), positionArray.get(2).getAsFloat());

		JsonElement rotationElement = shapeObject.get("rotation");
		if (!(rotationElement != null && rotationElement.isJsonArray()))
			throw new RuntimeException(String.format("PLM %s: 'shape.rotation' invalid element type", resourceLocation));
		JsonArray rotationArray = rotationElement.getAsJsonArray();
		if (rotationArray.size() != 3)
			throw new RuntimeException(String.format("PLM %s: 'shape.rotation' array length, expected 3 but found %d", resourceLocation, rotationArray.size()));
		Vector3f rotation = new Vector3f(rotationArray.get(0).getAsFloat(), rotationArray.get(1).getAsFloat(), rotationArray.get(2).getAsFloat());

		JsonElement sizeElement = shapeObject.get("size");
		if (!(sizeElement != null && sizeElement.isJsonArray()))
			throw new RuntimeException(String.format("PLM %s: 'shape.size' invalid element type", resourceLocation));
		JsonArray sizeArray = sizeElement.getAsJsonArray();
		if (sizeArray.size() != 3)
			throw new RuntimeException(String.format("PLM %s: 'shape.size' array length, expected 3 but found %d", resourceLocation, sizeArray.size()));
		Vector3f size = new Vector3f(sizeArray.get(0).getAsFloat(), sizeArray.get(1).getAsFloat(), sizeArray.get(2).getAsFloat());

		JsonElement pivotElement = shapeObject.get("pivot");
		if (!(pivotElement != null && pivotElement.isJsonArray()))
			throw new RuntimeException(String.format("PLM %s: 'shape.pivot' invalid element type", resourceLocation));
		JsonArray pivotArray = pivotElement.getAsJsonArray();
		if (pivotArray.size() != 3)
			throw new RuntimeException(String.format("PLM %s: 'shape.pivot' array length, expected 3 but found %d", resourceLocation, pivotArray.size()));
		Vector3f pivot = new Vector3f(pivotArray.get(0).getAsFloat(), pivotArray.get(1).getAsFloat(), pivotArray.get(2).getAsFloat());

		JsonElement facesElement = shapeObject.get("faces");
		if (!(facesElement != null && facesElement.isJsonObject()))
			throw new RuntimeException(String.format("PLM %s: 'shape.faces' invalid element type", resourceLocation));
		JsonObject facesObject = facesElement.getAsJsonObject();
		Map<String, Shape.Face> faces = new HashMap<>();
		facesObject.entrySet().forEach(entry -> {
			JsonElement faceElement = entry.getValue();
			if (!(faceElement != null && faceElement.isJsonObject()))
				throw new RuntimeException(String.format("PLM %s: 'shape.faces' contains invalid element types", resourceLocation));
			JsonObject faceObject = faceElement.getAsJsonObject();

			JsonElement rotationIndexElement = faceObject.get("rotation_index");
			if (!(rotationIndexElement != null && rotationIndexElement.isJsonPrimitive() && rotationIndexElement.getAsJsonPrimitive().isNumber()))
				throw new RuntimeException(String.format("PLM %s: 'shape.faces.rotation_index' invalid element type", resourceLocation));
			int rotationIndex = rotationIndexElement.getAsInt();

			JsonElement textureElement = faceObject.get("texture_index");
			if (!(textureElement != null && textureElement.isJsonPrimitive() && textureElement.getAsJsonPrimitive().isNumber()))
				throw new RuntimeException(String.format("PLM %s: 'shape.faces.texture_index' invalid element type", resourceLocation));
			int textureIndex = textureElement.getAsInt();

			JsonElement uvElement = faceObject.get("uv");
			if (!(uvElement != null && uvElement.isJsonArray()))
				throw new RuntimeException(String.format("PLM %s: 'shape.faces.uv' invalid element type", resourceLocation));
			JsonArray uvArray = uvElement.getAsJsonArray();
			if (uvArray.size() != 4)
				throw new RuntimeException(String.format("PLM %s: 'shape.faces.uv' array length, expected 4 but found %d", resourceLocation, uvArray.size()));
			int uvX = uvArray.get(0).getAsInt();
			int uvY = uvArray.get(1).getAsInt();
			int uvWidth = uvArray.get(2).getAsInt();
			int uvHeight = uvArray.get(3).getAsInt();

			faces.put(entry.getKey(), new Shape.Face(entry.getKey(), textureIndex, rotationIndex, uvX, uvY, uvWidth, uvHeight));
		});

		return new Shape(position, rotation, size, pivot, faces);
	}

	@Override
	public ResourceLocation getResourceID() {
		return ID;
	}
}
