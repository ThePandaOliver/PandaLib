package me.pandamods.pandalib.utils.animation.interpolation;

import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vector3Interpolator extends Interpolator<Vector3f> {
	public Vector3Interpolator(Vector3f value) {
		super(value);
	}

	@Override
	public Vector3f lerp(float time, Vector3f min, Vector3f max) {
		return min.lerp(max, (float) -Math.cos(time * Math.PI) / 2 + .5f, new Vector3f());
	}

	@Override
	public Vector3f getValue() {
		return super.getValue();
	}
}
