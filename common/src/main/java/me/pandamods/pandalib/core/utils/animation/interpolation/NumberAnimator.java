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
