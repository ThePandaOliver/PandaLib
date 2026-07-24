package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.codecs.JsonConfigCodec
import dev.pandasystems.pandalib.config.handle.ConfigHandle
import dev.pandasystems.pandalib.config.handle.DefaultConfigHandle
import dev.pandasystems.pandalib.config.store.FileConfigStore
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import java.nio.file.Path

object ConfigManager {
    inline fun <reified T> load(
        store: ConfigStore,
        noinline default: () -> T,
        codec: ConfigCodec = JsonConfigCodec(),
        serializer: KSerializer<T> = serializer()
    ): ConfigHandle<T> = loadInternal(store, default, codec, serializer)

    @PublishedApi
    internal fun <T> loadInternal(
        store: ConfigStore,
        default: () -> T,
        codec: ConfigCodec,
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