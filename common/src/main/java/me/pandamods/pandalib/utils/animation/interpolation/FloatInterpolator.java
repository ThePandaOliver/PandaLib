package me.pandamods.pandalib.utils.animation.interpolation;

import org.joml.Math;

public class FloatInterpolator extends Interpolator<Float> {
	public FloatInterpolator(float maxTime, Float startingValue, Float min, Float max) {
		super(maxTime, startingValue, min, max);
	}

	@Override
	public Float lerp(float time, Float min, Float max) {
		return Math.lerp(min, max, (float) -Math.cos(time * Math.PI) / 2 + .5f);
	}
}
