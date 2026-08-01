package dev.pandasystems.pandalib.config.store

class InMemoryConfigStore(initialContent: ByteArray? = null) : ConfigStore {
    private var content: ByteArray? = initialContent
    var writeCount: Int = 0
        private set

    override fun exists(): Boolean = content != null

    override fun read(): ByteArray =
        content ?: error("InMemoryConfigStore has no content to read")

    override fun writeAtomically(content: ByteArray) {
        this.content = content
        writeCount++
    }
}
