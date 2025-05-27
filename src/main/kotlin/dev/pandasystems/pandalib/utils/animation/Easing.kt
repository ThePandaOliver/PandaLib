
package dev.pandasystems.pandalib.utils.animation

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun interface Easing {
	fun ease(t: Double): Double

	companion object {
		@JvmStatic
		val easeInSine: Easing = Easing { t: Double -> 1 - cos((t * Math.PI) / 2) }
		@JvmStatic
		val easeOutSine: Easing = Easing { t: Double -> sin((t * Math.PI) / 2) }
		@JvmStatic
		val easeInOutSine: Easing = Easing { t: Double -> -(cos(Math.PI * t) - 1) / 2 }

		@JvmStatic
		val easeInQuad: Easing = Easing { t: Double -> t * t }
		@JvmStatic
		val easeOutQuad: Easing = Easing { t: Double -> 1 - (1 - t) * (1 - t) }
		@JvmStatic
		val easeInOutQuad: Easing = Easing { t: Double -> if (t < 0.5) 2 * t * t else 1 - (-2 * t + 2) * (-2 * t + 2) / 2 }

		@JvmStatic
		val easeInCubic: Easing = Easing { t: Double -> t * t * t }
		@JvmStatic
		val easeOutCubic: Easing = Easing { t: Double -> 1 - (1 - t).pow(3.0) }
		@JvmStatic
		val easeInOutCubic: Easing = Easing { t: Double -> if (t < 0.5) 4 * t * t * t else 1 - (-2 * t + 2).pow(3.0) / 2 }

		@JvmStatic
		val easeInQuart: Easing = Easing { t: Double -> t * t * t * t }
		@JvmStatic
		val easeOutQuart: Easing = Easing { t: Double -> 1 - (1 - t).pow(4.0) }
		@JvmStatic
		val easeInOutQuart: Easing = Easing { t: Double -> if (t < 0.5) 8 * t * t * t * t else 1 - (-2 * t + 2).pow(4.0) / 2 }

		@JvmStatic
		val easeInQuint: Easing = Easing { t: Double -> t * t * t * t * t }
		@JvmStatic
		val easeOutQuint: Easing = Easing { t: Double -> 1 - (1 - t).pow(5.0) }
		@JvmStatic
		val easeInOutQuint: Easing = Easing { t: Double -> if (t < 0.5) 16 * t * t * t * t * t else 1 - (-2 * t + 2).pow(5.0) / 2 }

		@JvmStatic
		val easeInExpo: Easing = Easing { t: Double -> if (t == 0.0) 0.0 else 2.0.pow(10 * (t - 1)) }
		@JvmStatic
		val easeOutExpo: Easing = Easing { t: Double -> if (t == 1.0) 1.0 else 1 - 2.0.pow(-10 * t) }
		@JvmStatic
		val easeInOutExpo: Easing =
			Easing { t: Double -> if (t == 0.0) 0.0 else if (t == 1.0) 1.0 else if (t < 0.5) 2.0.pow(20 * t - 10) / 2 else (2 - 2.0.pow(-20 * t + 10)) / 2 }

		@JvmStatic
		val easeInCirc: Easing = Easing { t: Double -> 1 - sqrt(1 - t * t) }
		@JvmStatic
		val easeOutCirc: Easing = Easing { t: Double -> sqrt(1 - (t - 1).pow(2.0)) }
		@JvmStatic
		val easeInOutCirc: Easing = Easing { t: Double -> if (t < 0.5) (1 - sqrt(1 - 4 * t * t)) / 2 else (sqrt(1 - -4 * t * t) + 1) / 2 }

		@JvmStatic
		val easeInBack: Easing = Easing { t: Double ->
			val c1 = 1.70158
			val c3 = c1 + 1
			c3 * t * t * t - c1 * t * t
		}
		@JvmStatic
		val easeOutBack: Easing = Easing { t: Double ->
			val c1 = 1.70158
			val c3 = c1 + 1
			1 + c3 * (t - 1) * (t - 1) * (t - 1) - c1 * (t - 1) * (t - 1)
		}
		@JvmStatic
		val easeInOutBack: Easing = Easing { t: Double ->
			val c1 = 1.70158
			val c2 = c1 * 1.525
			if (t < 0.5)
				((2 * t).pow(2.0) * ((c2 + 1) * 2 * t - c2)) / 2
			else
				((2 * t - 2).pow(2.0) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2
		}

		@JvmStatic
		val easeInElastic: Easing = Easing { t: Double ->
			val c4 = (2 * Math.PI) / 3
			when (t) {
				0.0 -> 0.0
				1.0 -> 1.0
				else -> (-2.0).pow(10 * t - 10) * sin((t * 10 - 10.75) * c4)
			}
		}
		@JvmStatic
		val easeOutElastic: Easing = Easing { t: Double ->
			val c4 = (2 * Math.PI) / 3
			when (t) {
				0.0 -> 0.0
				1.0 -> 1.0
				else -> 2.0.pow(-10 * t) * sin((t * 10 - 0.75) * c4) + 1
			}
		}
		@JvmStatic
		val easeInOutElastic: Easing = Easing { t: Double ->
			val c5 = (2 * Math.PI) / 4.5
			when (t) {
				0.0 -> 0.0
				1.0 -> 1.0
				else -> {
					if (t < 0.5)
						-(2.0.pow(20 * t - 10) * sin((20 * t - 11.125) * c5)) / 2
					else
						(2.0.pow(-20 * t + 10) * sin((20 * t - 11.125) * c5)) / 2 + 1
				}
			}
		}

		@JvmStatic
		val easeOutBounce: Easing = Easing { t: Double ->
			var t = t
			val n1 = 7.5625
			val d1 = 2.75
			return@Easing if (t < 1 / d1) {
				n1 * t * t
			} else if (t < 2 / d1) {
				n1 * (1.5 / d1.let { t -= it; t }) * t + 0.75
			} else if (t < 2.5 / d1) {
				n1 * (2.25 / d1.let { t -= it; t }) * t + 0.9375
			} else {
				n1 * (2.625 / d1.let { t -= it; t }) * t + 0.984375
			}
		}
		@JvmStatic
		val easeInBounce: Easing = Easing { t: Double -> 1 - easeOutBounce.ease(1 - t) }
		@JvmStatic
		val easeInOutBounce: Easing = Easing { t: Double -> if (t < 0.5) (1 - easeOutBounce.ease(1 - 2 * t)) / 2 else (1 + easeOutBounce.ease(2 * t - 1)) / 2 }
	}
}
