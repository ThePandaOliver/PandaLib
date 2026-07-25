package dev.pandasystems.pandalib.config.store

interface ConfigStore {
    fun exists(): Boolean
    fun read(): ByteArray
    fun writeAtomically(content: ByteArray)
}