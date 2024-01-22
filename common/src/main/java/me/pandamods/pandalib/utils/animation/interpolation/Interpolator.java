package me.pandamods.pandalib.utils.animation.interpolation;

import me.pandamods.pandalib.utils.RenderUtils;
import org.joml.Math;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Interpolator<T> {
	private float time = 0;
	private float duration = 1;
	private T previous = null;
	private T next;

	public Interpolator(T value) {
		this.next = value;
	}

	public final void update() {
		this.update(1);
	}

	public final void updateReverse() {
		this.update(-1);
	}

	public final void update(float multiplier) {
		time += RenderUtils.getDeltaSeconds() * multiplier;
		time = Math.clamp(0, duration, time);
	}

	public abstract T lerp(float time, T min, T max);

	protected T getValue() {
		if (previous == null) return next;
		return this.lerp(time / duration, previous, next);
	}

	@SuppressWarnings("unchecked")
	public final <E extends Interpolator<T>> E setTarget(T next) {
		if (!equals(this.next, next)) {
			this.time = 0;
			this.previous = this.getValue();
			this.next = next;
		}
		return (E) this;
	}

	public boolean equals(T a, T b) {
		return a.equals(b);
	}

	@SuppressWarnings("unchecked")
	public <E extends Interpolator<T>> E setTime(float time) {
		this.time = time;
		return (E) this;
	}

	public float getTime() {
		return time;
	}

	@SuppressWarnings("unchecked")
	public <E extends Interpolator<T>> E setDuration(float duration) {
		this.duration = duration;
		return (E) this;
	}

	public float getDuration() {
		return duration;
	}
}
