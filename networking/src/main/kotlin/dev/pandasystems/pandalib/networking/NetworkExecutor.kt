package dev.pandasystems.pandalib.networking

fun interface NetworkExecutor {
    fun execute(task: () -> Unit)
}