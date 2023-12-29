package me.pandamods.pandalib.utils.animation.interpolation;

import me.pandamods.pandalib.utils.RenderUtils;
import org.joml.Math;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Interpolator<T> {
	private float time = 0;
	private final float maxTime;
	private T value;
	private T min;
	private T max;

	public Interpolator(float maxTime, T startingValue, T min, T max) {
		this.maxTime = maxTime;
		this.value = startingValue;
		this.min = min;
		this.max = max;
	}

	public final void update() {
		this.update(1);
	}

	public final void updateReverse() {
		this.update(-1);
	}

	public final void update(float multiplier) {
		time += RenderUtils.getDeltaSeconds() * multiplier;
		time = Math.clamp(0, maxTime, time);

		value = this.lerp(time / maxTime, min, max);
	}

	public abstract T lerp(float time, T min, T max);

	public float getTime() {
		return time;
	}

	public T getValue() {
		return value;
	}

	public final void setTarget(T target) {
		if (!equals(max, target)) {
			this.time = 0;
			this.min = this.value;
			this.max = target;
		}
	}

	public boolean equals(T a, T b) {
		return a.equals(b);
	}

	public void setMin(T min) {
		this.min = min;
	}

	public void setMax(T max) {
		this.max = max;
	}

	public void setBounds(T min, T max) {
		this.min = min;
		this.max = max;
	}
}
