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

package me.pandamods.pandalib.client.resource.loader;

import me.pandamods.pandalib.client.resource.model.Bone;
import me.pandamods.pandalib.client.resource.model.Mesh;
import me.pandamods.pandalib.client.resource.model.Model;
import me.pandamods.pandalib.utils.AssimpUtils;
import me.pandamods.pandalib.utils.CollectionsUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelLoader {
	public static Model loadFromScene(AIScene scene) {
		List<Mesh> meshes = new ArrayList<>();
		List<String> textures = new ArrayList<>();
		Map<String, Bone> bones = new HashMap<>();

		AINode rootNode = scene.mRootNode();
		if (rootNode != null)
			buildBoneHierarchy(rootNode, null, bones);

		PointerBuffer materialsPointer = scene.mMaterials();
		if (materialsPointer != null) {
			for (int i = 0; i < scene.mNumMaterials(); i++) {
				AIMaterial aiMaterial = AIMaterial.create(materialsPointer.get(i));
				AIString materialName = AIString.create();
				Assimp.aiGetMaterialString(aiMaterial, Assimp.AI_MATKEY_NAME, Assimp.aiTextureType_NONE, 0, materialName);
				String materialNameStr = materialName.dataString();
				textures.add(materialNameStr);
			}
		}

		PointerBuffer meshesPointer = scene.mMeshes();
		if (meshesPointer != null) {
			for (int i = 0; i < scene.mNumMeshes(); i++) {
				AIMesh aiMesh = AIMesh.create(meshesPointer.get(i));
				meshes.add(processMesh(aiMesh, bones));
			}
		}

		return new Model(meshes, textures, bones);
	}

	private static void buildBoneHierarchy(AINode aiNode, Bone parent, Map<String, Bone> bones) {
		String boneName = aiNode.mName().dataString();
		Matrix4f offsetTransform = AssimpUtils.toMatrix4f(aiNode.mTransformation());

		Bone bone = new Bone(boneName, parent, offsetTransform);
		bones.put(boneName, bone);

		PointerBuffer nodeChildrenPointer = aiNode.mChildren();
		if (nodeChildrenPointer != null) {
			for (int i = 0; i < aiNode.mNumChildren(); i++) {
				AINode aiChildNode = AINode.create(nodeChildrenPointer.get(i));
				buildBoneHierarchy(aiChildNode, bone, bones);
			}
		}
	}

	private static Mesh processMesh(AIMesh aiMesh, Map<String, Bone> bones) {
		List<Mesh.Vertex> vertices = new ArrayList<>();
		List<List<Mesh.VertexWeight>> vertexWeights = new ArrayList<>();
		Bone meshBone = bones.get(aiMesh.mName().dataString());

		for (int i = 0; i < aiMesh.mNumVertices(); i++) {
			vertexWeights.add(new ArrayList<>());
		}

		PointerBuffer bonesPointer = aiMesh.mBones();
		if (bonesPointer != null) {
			for (int i = 0; i < aiMesh.mNumBones(); i++) {
				AIBone aiBone = AIBone.create(bonesPointer.get(i));

				for (int j = 0; j < aiBone.mNumWeights(); j++) {
					AIVertexWeight aiVertexWeight = aiBone.mWeights().get(j);
					int boneIndex = CollectionsUtils.findIndexOf(bones.keySet(), aiBone.mName().dataString());
					vertexWeights.get(aiVertexWeight.mVertexId()).add(new Mesh.VertexWeight(boneIndex, aiVertexWeight.mWeight()));
				}
			}

			for (List<Mesh.VertexWeight> weights : vertexWeights) {
				float totalWeight = (float) weights.stream().mapToDouble(Mesh.VertexWeight::weight).sum();
				if (totalWeight > 0.0f) {
					for (int k = 0; k < weights.size(); k++) {
						Mesh.VertexWeight weight = weights.get(k);
						weights.set(k, new Mesh.VertexWeight(weight.boneIndex(), weight.weight() / totalWeight));
					}
				}
			}
		}

		for (int i = 0; i < aiMesh.mNumVertices(); i++) {
			AIVector3D aiPosition = aiMesh.mVertices().get(i);
			AIVector3D aiNormal = aiMesh.mNormals().get(i);
			AIVector3D aiTextureCoords = aiMesh.mTextureCoords(0).get(i);

			Color color = Color.WHITE;

			AIColor4D.Buffer aiColorBuffer = aiMesh.mColors(0);
			if (aiColorBuffer != null) {
				AIColor4D aiColor = aiColorBuffer.get(i);
				color = new Color(aiColor.r(), aiColor.g(), aiColor.b(), aiColor.a());
			}

			Vector3f position = new Vector3f(aiPosition.x(), aiPosition.y(), aiPosition.z());
			Vector3f normal = new Vector3f(aiNormal.x(), aiNormal.y(), aiNormal.z());

			if (meshBone != null) {
				Matrix4f boneTransform = meshBone.getGlobalTransform();
				boneTransform.transformPosition(position);
				boneTransform.transformDirection(normal);
			}

			vertices.add(new Mesh.Vertex(
					position.x(), position.y(), position.z(),
					normal.x(), normal.y(), normal.z(),
					aiTextureCoords.x(), aiTextureCoords.y(), color,
					vertexWeights.get(i)
			));
		}

		List<Mesh.Vertex> finalVertices = new ArrayList<>();
		for (int i = 0; i < aiMesh.mNumFaces(); i++) {
			AIFace aiFace = aiMesh.mFaces().get(i);
			for (int j = 0; j < aiFace.mNumIndices(); j++) {
				int index = aiFace.mIndices().get(j);
				finalVertices.add(vertices.get(index));
			}
		}

		return new Mesh(finalVertices, aiMesh.mMaterialIndex());
	}
}
