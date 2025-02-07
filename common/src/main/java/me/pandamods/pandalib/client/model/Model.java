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

import java.util.List;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class Model {
	private final int textureWidth;
	private final int textureHeight;

	private final List<String> textures;

	private final Node rootNode;
	private final List<Node> nodes;

	public Model(int textureWidth, int textureHeight, List<String> textures, Node rootNode, List<Node> nodes) {
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.textures = textures;
		this.rootNode = rootNode;
		this.nodes = nodes;
	}

	public void render(PoseStack stack, Function<String, VertexConsumer> getVertexConsumer) {
		this.rootNode.render(this, stack, getVertexConsumer);
	}

	public List<String> getTextures() {
		return textures;
	}
}
