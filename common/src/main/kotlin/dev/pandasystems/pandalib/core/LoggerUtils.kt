package dev.pandasystems.pandalib.core

import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

private val lastLogTimes = ConcurrentHashMap<String, AtomicLong>()
private val suppressedCounts = ConcurrentHashMap<String, AtomicInteger>()

/**
 * Logs an info message at most once every [intervalMs] for the given [key].
 * If suppressed messages occurred during the window, the count is appended.
 */
fun Logger.infoThrottled(
    key: String,
    intervalMs: Long = 5000L,
    messageSupplier: () -> String
) {
    if (!isInfoEnabled) return

    val now = System.currentTimeMillis()
    val lastTime = lastLogTimes.computeIfAbsent(key) { AtomicLong(0L) }
    val count = suppressedCounts.computeIfAbsent(key) { AtomicInteger(0) }

    val prev = lastTime.get()
    if (now - prev >= intervalMs) {
        if (lastTime.compareAndSet(prev, now)) {
            val suppressed = count.getAndSet(0)
            val msg = messageSupplier()
            if (suppressed > 0) {
                this.info("$msg (repeated $suppressed times in last ${intervalMs / 1000}s)")
            } else {
                this.info(msg)
            }
            return
        }
    }
    count.incrementAndGet()
}

/**
 * Logs a debug message at most once every [intervalMs] for the given [key].
 */
fun Logger.debugThrottled(
    key: String,
    intervalMs: Long = 5000L,
    messageSupplier: () -> String
) {
    if (!isDebugEnabled) return
    infoThrottled(key, intervalMs, messageSupplier)
}