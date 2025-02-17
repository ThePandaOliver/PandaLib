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

import java.awt.*;
import java.util.List;

public record Mesh(List<Vertex> vertices, int textureIndex) {
	public record Vertex(
			float x, float y, float z,
			float normX, float normY, float normZ,
			float texU, float texV, Color color,
			List<VertexWeight> weights
	) {}

	public record VertexWeight(int boneIndex, float weight) {}
}
