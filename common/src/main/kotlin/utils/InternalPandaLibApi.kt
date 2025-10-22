package dev.pandasystems.pandalib.utils

@RequiresOptIn(
    message = "This API is internal to PandaLib and should not be used outside of PandaLib modules.",
    level = RequiresOptIn.Level.WARNING
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.TYPEALIAS
)
annotation class InternalPandaLibApi