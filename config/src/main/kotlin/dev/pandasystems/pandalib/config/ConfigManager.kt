package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.handle.ConfigHandle
import dev.pandasystems.pandalib.config.handle.DefaultConfigHandle
import kotlinx.io.files.Path
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

object ConfigManager {
    inline fun <reified T> load(
        store: ConfigStore,
        codec: ConfigCodec,
        noinline default: () -> T
    ): ConfigHandle<T> = load(store, codec, default, serializer())

    fun <T> load(
        store: ConfigStore,
        codec: ConfigCodec,
        default: () -> T,
        serializer: KSerializer<T>
    ): ConfigHandle<T> {
        val handle = DefaultConfigHandle(
            store = store,
            codec = codec,
            serializer = serializer,
            default = default,
        )

        handle.reload()
        return handle
    }
}