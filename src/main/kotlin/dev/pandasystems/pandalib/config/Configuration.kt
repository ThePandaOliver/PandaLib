

package dev.pandasystems.pandalib.config

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Configuration(
    val modId: String,
    val pathName: String
)