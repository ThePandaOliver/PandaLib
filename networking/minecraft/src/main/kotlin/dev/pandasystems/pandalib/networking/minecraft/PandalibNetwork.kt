package dev.pandasystems.pandalib.networking.minecraft

import dev.pandasystems.pandalib.networking.NetworkRegistrar

internal class PandalibNetwork(
    val registrar: NetworkRegistrar
) {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: PandalibNetwork
            private set
    }
}