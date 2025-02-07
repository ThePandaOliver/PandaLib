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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class Node {
	private final String name;

	private Node parent = null;
	private final List<Node> children = new ArrayList<>();
	private final List<Node> viewChildren = Collections.unmodifiableList(this.children);

	private final Shape shape;

	public Node(String name, Node parent, Shape shape) {
		this.name = name;

		if (parent != null)
			setParent(parent);

		this.shape = shape;
	}

	public void setParent(Node parent) {
		if (this.parent == parent) return;
		if (this.parent != null)
			this.parent.children.remove(this);
		this.parent = parent;
		if (parent != null)
			parent.children.add(this);
	}

	public List<Node> getViewChildren() {
		return viewChildren;
	}

	public void render(Model model, PoseStack stack, Function<String, VertexConsumer> getVertexConsumer) {
		System.out.println(this.name);
		if (this.shape != null) {
			this.shape.render(model, stack, getVertexConsumer);
		}

		this.children.forEach(child -> child.render(model, stack, getVertexConsumer));
	}
}
