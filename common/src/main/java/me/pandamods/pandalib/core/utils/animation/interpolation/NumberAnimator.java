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

import org.joml.Math;

public class NumberAnimator extends Animator<Number> {
	public NumberAnimator(Number value) {
		super(value);
	}

	@Override
	public Number lerp(float time, Number min, Number max) {
		return Math.lerp(min.floatValue(), max.floatValue(), time);
	}

	public float getAsFloat() {
		return super.getValue().floatValue();
	}

	public double getAsDouble() {
		return super.getValue().doubleValue();
	}

	public int getAsInt() {
		return super.getValue().intValue();
	}

	public byte getAsByte() {
		return super.getValue().byteValue();
	}

	public short getAsShort() {
		return super.getValue().shortValue();
	}

	public long getAsLong() {
		return super.getValue().longValue();
	}
}
