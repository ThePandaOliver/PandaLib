/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

@file:JvmName("EasingUtils")

package dev.pandasystems.pandalib.utils.animation

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun interface Easing {
	fun ease(t: Double): Double
}

val easeInSine = Easing { t -> 1 - cos((t * Math.PI) / 2) }
val easeOutSine = Easing { t -> sin((t * Math.PI) / 2) }
val easeInOutSine = Easing { t -> -(cos(Math.PI * t) - 1) / 2 }

val easeInQuad = Easing { t -> t * t }
val easeOutQuad = Easing { t -> 1 - (1 - t) * (1 - t) }
val easeInOutQuad = Easing { t -> if (t < 0.5) 2 * t * t else 1 - (-2 * t + 2) * (-2 * t + 2) / 2 }

val easeInCubic = Easing { t -> t * t * t }
val easeOutCubic = Easing { t -> 1 - (1 - t).pow(3.0) }
val easeInOutCubic = Easing { t -> if (t < 0.5) 4 * t * t * t else 1 - (-2 * t + 2).pow(3.0) / 2 }

val easeInQuart = Easing { t -> t * t * t * t }
val easeOutQuart = Easing { t -> 1 - (1 - t).pow(4.0) }
val easeInOutQuart = Easing { t -> if (t < 0.5) 8 * t * t * t * t else 1 - (-2 * t + 2).pow(4.0) / 2 }

val easeInQuint = Easing { t -> t * t * t * t * t }
val easeOutQuint = Easing { t -> 1 - (1 - t).pow(5.0) }
val easeInOutQuint = Easing { t -> if (t < 0.5) 16 * t * t * t * t * t else 1 - (-2 * t + 2).pow(5.0) / 2 }

val easeInExpo = Easing { t -> if (t == 0.0) 0.0 else 2.0.pow(10 * (t - 1)) }
val easeOutExpo = Easing { t -> if (t == 1.0) 1.0 else 1 - 2.0.pow(-10 * t) }
val easeInOutExpo = Easing { t -> 
	when {
		t == 0.0 -> 0.0
		t == 1.0 -> 1.0
		t < 0.5 -> 2.0.pow(20 * t - 10) / 2
		else -> (2 - 2.0.pow(-20 * t + 10)) / 2
	}
}

val easeInCirc = Easing { t -> 1 - sqrt(1 - t * t) }
val easeOutCirc = Easing { t -> sqrt(1 - (t - 1).pow(2.0)) }
val easeInOutCirc = Easing { t -> if (t < 0.5) (1 - sqrt(1 - 4 * t * t)) / 2 else (sqrt(1 - -4 * t * t) + 1) / 2 }

val easeInBack = Easing { t ->
	val c1 = 1.70158
	val c3 = c1 + 1
	c3 * t * t * t - c1 * t * t
}
val easeOutBack = Easing { t ->
	val c1 = 1.70158
	val c3 = c1 + 1
	1 + c3 * (t - 1) * (t - 1) * (t - 1) - c1 * (t - 1) * (t - 1)
}
val easeInOutBack = Easing { t ->
	val c1 = 1.70158
	val c2 = c1 * 1.525
	if (t < 0.5)
		((2 * t).pow(2.0) * ((c2 + 1) * 2 * t - c2)) / 2
	else
		((2 * t - 2).pow(2.0) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2
}

val easeInElastic = Easing { t ->
	val c4 = (2 * Math.PI) / 3
	when (t) {
		0.0 -> 0.0
		1.0 -> 1.0
		else -> (-2.0).pow(10 * t - 10) * sin((t * 10 - 10.75) * c4)
	}
}
val easeOutElastic = Easing { t ->
	val c4 = (2 * Math.PI) / 3
	when (t) {
		0.0 -> 0.0
		1.0 -> 1.0
		else -> 2.0.pow(-10 * t) * sin((t * 10 - 0.75) * c4) + 1
	}
}
val easeInOutElastic = Easing { t ->
	val c5 = (2 * Math.PI) / 4.5
	when {
		t == 0.0 -> 0.0
		t == 1.0 -> 1.0
		t < 0.5 -> -(2.0.pow(20 * t - 10) * sin((20 * t - 11.125) * c5)) / 2
		else -> (2.0.pow(-20 * t + 10) * sin((20 * t - 11.125) * c5)) / 2 + 1
	}
}

val easeOutBounce = Easing { t ->
	var t = t
	val n1 = 7.5625
	val d1 = 2.75
	when {
		t < 1 / d1 -> n1 * t * t
		t < 2 / d1 -> n1 * (1.5 / d1.let { t -= it; t }) * t + 0.75
		t < 2.5 / d1 -> n1 * (2.25 / d1.let { t -= it; t }) * t + 0.9375
		else -> n1 * (2.625 / d1.let { t -= it; t }) * t + 0.984375
	}
}

val easeInBounce = Easing { t -> 1 - easeOutBounce.ease(1 - t) }
val easeInOutBounce = Easing { t -> if (t < 0.5) (1 - easeOutBounce.ease(1 - 2 * t)) / 2 else (1 + easeOutBounce.ease(2 * t - 1)) / 2 }
