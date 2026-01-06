/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.utils

// TODO: Make this class follow the Semantic Versioning standard
data class Version(
	val major: Int,
	val minor: Int,
	val patch: Int,
) {
	override fun toString(): String = "$major.$minor.$patch"

	companion object {
		lateinit var minecraft: Version
			internal set

		val mc1_21_11 = Version(1, 21, 11)
		val mc1_21_10 = Version(1, 21, 10)
		val mc1_21_9 = Version(1, 21, 9)
		val mc1_21_8 = Version(1, 21, 8)
		val mc1_21_7 = Version(1, 21, 7)
		val mc1_21_6 = Version(1, 21, 6)
		val mc1_21_5 = Version(1, 21, 5)
		val mc1_21_4 = Version(1, 21, 4)
		val mc1_21_3 = Version(1, 21, 3)
		val mc1_21_2 = Version(1, 21, 2)
		val mc1_21_1 = Version(1, 21, 1)
		val mc1_21 = Version(1, 21, 0)

		val mc1_20_6 = Version(1, 20, 6)
		val mc1_20_5 = Version(1, 20, 5)
		val mc1_20_4 = Version(1, 20, 4)
		val mc1_20_3 = Version(1, 20, 3)
		val mc1_20_2 = Version(1, 20, 2)
		val mc1_20_1 = Version(1, 20, 1)
		val mc1_20 = Version(1, 20, 0)
	}
}