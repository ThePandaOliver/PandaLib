package dev.pandasystems.pandalib.config.handle

interface ConfigHandle<T> {
    val value: T

    fun reload(): T

    fun save()

    fun update(transform: (T) -> T): T
}