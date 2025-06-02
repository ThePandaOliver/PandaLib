@file:JvmName("ClassUtils")

package dev.pandasystems.pandalib.utils

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

/**
 * Replaces the singleton instance of an object in Kotlin.
 * This is a workaround for cases where you need to change the singleton instance at runtime.
 *
 * @param newInstance The new instance to set as the singleton.
 */
fun <T : Any> KClass<out T>.replaceObjectInstanceUnsafely(newInstance: T) {
	// TODO: Make some checks to make sure the instance is the same type and really is a singleton
	try {
		val instanceProperty = this.memberProperties
			.find { it.name == "INSTANCE" }

		instanceProperty?.let { prop ->
			prop.isAccessible = true
			val javaField = prop.javaField
			javaField?.let { field ->
				field.isAccessible = true

				// Remove final modifier (same as Java approach)
				val modifiersField = Field::class.java.getDeclaredField("modifiers")
				modifiersField.isAccessible = true
				modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())

				field.set(null, newInstance)
			}
		}
	} catch (e: Exception) {
		e.printStackTrace()
	}
}