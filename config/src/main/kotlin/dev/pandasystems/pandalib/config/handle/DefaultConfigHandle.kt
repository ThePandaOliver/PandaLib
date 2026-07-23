package dev.pandasystems.pandalib.config.handle

import dev.pandasystems.pandalib.config.ConfigCodec
import dev.pandasystems.pandalib.config.ConfigStore
import kotlinx.serialization.KSerializer

internal class DefaultConfigHandle<T>(
    private val store: ConfigStore,
    private val codec: ConfigCodec,
    private val serializer: KSerializer<T>,
    private val default: () -> T
) : ConfigHandle<T> {
    private var currentValue: T = default()

    override val value: T
        get() = currentValue

    override fun reload(): T {
        currentValue = if (store.exists()) {
            codec.decode(serializer, store.read())
        } else {
            default().also { created ->
                store.writeAtomically(codec.encode(serializer, created))
            }
        }

        return currentValue
    }

    override fun save() {
        store.writeAtomically(codec.encode(serializer, currentValue))
    }

    override fun update(transform: (T) -> T): T {
        currentValue = transform(currentValue)
        return currentValue
    }
}