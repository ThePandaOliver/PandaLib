package me.pandamods.pandalib.core.utils.animation.interpolation;

import org.joml.Math;

public class NumberInterpolator extends Interpolator<Number> {
	public NumberInterpolator(Number value) {
		super(value);
	}

	@Override
	public Number lerp(float time, Number min, Number max) {
		return Math.lerp(min.floatValue(), max.floatValue(), (float) -Math.cos(time * Math.PI) / 2 + .5f);
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
