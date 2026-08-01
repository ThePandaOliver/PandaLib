package dev.pandasystems.pandalib.core

class PandaLibCore() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: PandaLibCore
    }
}