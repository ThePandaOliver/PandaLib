/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.core.utils.animation.interpolation;

import com.mojang.blaze3d.Blaze3D;
import me.pandamods.pandalib.api.client.screen.PLScreen;
import net.minecraft.client.Minecraft;
import org.joml.Math;

import java.util.Objects;

public abstract class Animator<T> {
	private float time = 0;
	private float duration = 1;
	private T previous = null;
	private T next;
	private boolean skipFirst = false;

	public Animator(T value) {
		this.next = value;
	}

	public final void update() {
		this.update(1);
	}

	public final void updateReverse() {
		this.update(-1);
	}

	public final void update(float multiplier) {
		time += PLScreen.getDeltaSeconds() * multiplier;
		time = Math.clamp(0, duration, time);
	}

	public abstract T lerp(float time, T min, T max);

	public T getValue() {
		if (previous == null) return next;
		return this.lerp(time / duration, previous, next);
	}

	@SuppressWarnings("unchecked")
	public final <E extends Animator<T>> E setTarget(T next) {
		if (!equals(this.next, next)) {
			T value = this.getValue();
			boolean shouldSkip = skipFirst && this.previous == null;
			this.time = shouldSkip ? 1 : 0;
			this.previous = value;
			this.next = next;
		}
		return (E) this;
	}

	public boolean equals(T a, T b) {
		return Objects.equals(a, b);
	}

	@SuppressWarnings("unchecked")
	public <E extends Animator<T>> E setTime(float time) {
		this.time = time;
		return (E) this;
	}

	public float getTime() {
		return time;
	}

	@SuppressWarnings("unchecked")
	public <E extends Animator<T>> E setDuration(float duration) {
		this.duration = duration;
		return (E) this;
	}

	public float getDuration() {
		return duration;
	}

	@SuppressWarnings("unchecked")
	public <E extends Animator<T>> E skipFirst() {
		this.skipFirst = true;
		return (E) this;
	}
}
