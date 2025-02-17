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

package me.pandamods.pandalib.client.resource.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Model {
	private final List<Mesh> meshes;
	private final List<String> textures;
	private final Map<String, Bone> bones;

	public Model(List<Mesh> meshes, List<String> textures, Map<String, Bone> bones) {
		this.meshes = Collections.unmodifiableList(meshes);
		this.textures = Collections.unmodifiableList(textures);
		this.bones = Collections.unmodifiableMap(bones);
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}

	public List<String> getTextures() {
		return textures;
	}

	public Map<String, Bone> getBones() {
		return bones;
	}
}