package me.pandamods.pandalib.utils.animation.interpolation;

import org.joml.Math;
import org.joml.Vector2f;

public class Vector2Interpolator extends Interpolator<Vector2f> {
	public Vector2Interpolator(float maxTime, Vector2f startingValue, Vector2f max) {
		super(maxTime, startingValue, max);
	}

	@Override
	public Vector2f lerp(float time, Vector2f min, Vector2f max) {
		return min.lerp(max, (float) -Math.cos(time * Math.PI) / 2 + .5f, new Vector2f());
	}

	@Override
	public Vector2f getValue() {
		return new Vector2f(super.getValue());
	}
}
