package dev.pandasystems.pandalib.impl.config

import kotlinx.serialization.Serializable

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Configuration(
	/**
	 * The ID of the mod the config should be registered under
	 */
	val modId: String,

	/**
	 * Path of where the config will be stored.
	 *
	 * Examples:
	 * - `config_name` will be located at `config/config_name.json`
	 * - `mod_id/config_name` will be located at `config/mod_id/config_name.json`
	 */
	val pathName: String
)