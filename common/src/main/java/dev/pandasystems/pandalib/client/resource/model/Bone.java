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

package dev.pandasystems.pandalib.client.resource.model;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bone {
	private final String name;
	private final Bone parent;

	private final Matrix4f offsetTransform;
	private final Matrix4f globalOffsetTransform = new Matrix4f();

	private final Matrix4f localTransform = new Matrix4f();

	private final Matrix4f globalTransform = new Matrix4f();
	private boolean isDirty = true;

	private List<Bone> children;
	private List<Bone> viewChildren;

	public Bone(String name, Bone parent, Matrix4f offsetTransform) {
		this.name = name;
		this.parent = parent;
		this.offsetTransform = offsetTransform;
		this.children = new ArrayList<>();
		this.viewChildren = Collections.unmodifiableList(children);

		if (parent != null) {
			parent.children.add(this);
		}

		if (parent != null) {
			globalOffsetTransform.mul(parent.getGlobalOffsetTransform());
		}
		globalOffsetTransform.mul(offsetTransform);
	}

	public void markDirty() {
		isDirty = true;
		children.forEach(Bone::markDirty);
	}

	private void calculateGlobalTransform() {
		globalTransform.identity();
		if (parent != null) {
			globalTransform.mul(parent.getGlobalTransform());
		}
		globalTransform.mul(offsetTransform).mul(localTransform);
		isDirty = false;
	}

	public Matrix4f getGlobalTransform() {
		if (isDirty)
			calculateGlobalTransform();
		return globalTransform;
	}

	public Matrix4f getGlobalOffsetTransform() {
		return globalOffsetTransform;
	}

	public Matrix4f getLocalTransform() {
		return localTransform;
	}

	public Matrix4f getOffsetTransform() {
		return offsetTransform;
	}

	public void setLocalTransform(Matrix4f localTransform) {
		markDirty();
		this.localTransform.set(localTransform);
	}

	public List<Bone> getChildren() {
		return viewChildren;
	}

	public Bone getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}
}
