package dev.pandasystems.pandalib.config.store

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.readBytes

class FileConfigStore(
    private val path: Path
) : ConfigStore {
    override fun exists(): Boolean = path.exists()

    override fun read(): ByteArray = path.readBytes()

    override fun writeAtomically(content: ByteArray) {
        val directory = path.parent
            ?: throw IOException("Config path must have a parent directory: $path")

        Files.createDirectories(directory)

        val temporaryPath = Files.createTempFile(
            directory,
            "${path.fileName}.",
            ".tmp",
        )

        try {
            writeAndSync(temporaryPath, content)
            replaceTarget(temporaryPath)
        } finally {
            temporaryPath.deleteIfExists()
        }
    }

    private fun writeAndSync(temporaryPath: Path, content: ByteArray) {
        FileChannel.open(
            temporaryPath,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING,
        ).use { channel ->
            val buffer = ByteBuffer.wrap(content)

            while (buffer.hasRemaining()) {
                channel.write(buffer)
            }

            channel.force(true)
        }
    }

    private fun replaceTarget(temporaryPath: Path) {
        try {
            Files.move(
                temporaryPath,
                path,
                StandardCopyOption.ATOMIC_MOVE,
                StandardCopyOption.REPLACE_EXISTING,
            )
        } catch (_: AtomicMoveNotSupportedException) {
            Files.move(
                temporaryPath,
                path,
                StandardCopyOption.REPLACE_EXISTING,
            )
        }
    }
}