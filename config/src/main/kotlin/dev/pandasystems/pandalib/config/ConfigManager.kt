package dev.pandasystems.pandalib.config

import kotlinx.io.files.Path
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

object ConfigManager {
    inline fun <reified T> load(
        path: Path,
        codec: ConfigCodec,
        noinline default: () -> T,
        serializer: KSerializer<T> = serializer()
    ) {

    }
}