/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
package dev.pandasystems.pandalib.config

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Option(
	@Deprecated("Not implemented yet")
	val comment: String = "",

	@Deprecated("Not implemented yet")
	/**
	 * Warning message to display when this option is changed.
	 * Useful for options that may have unintended side effects or require special handling.
	 */
	val warning: String = "",

	@Deprecated("Not implemented yet")
	/**
	 * Indicates whether changing this option requires a game restart to take effect.
	 */
	val requireGameRestart: Boolean = false,

	@Deprecated("Not implemented yet")
	/**
	 * Indicates whether changing this option requires a world restart to take effect.
	 */
	val requireWorldRestart: Boolean = false,
)
