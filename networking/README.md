# PandaLib Networking

The `networking` module is loader-independent. Define packets with a codec and
a direction, then register them through the loader implementation. Packet IDs
must be unique and should use your mod id as their namespace.

```kotlin
@Serializable
data class OpenScreen(val title: String)

val openScreen = clientboundPacket(
    PacketId.of("examplemod", "open_screen"),
    CborPacketCodec(OpenScreen.serializer()),
)

val network = FabricNetworkRegistrar()

fun initialize() {
    network.register(openScreen) { context, packet ->
        context.executor.execute { openMyScreen(packet.title) }
    }
}

fun tellPlayer(player: ServerPlayer) {
    network.sendToPeer(MinecraftNetworkPeer(player), openScreen, OpenScreen("Hello"))
}
```

Use `serverboundPacket` for packets sent with `network.sendToServer`. A handler
can call `context.reply(...)`; the library sends the reply to the other side of
the same connection. The current Fabric implementation covers the Minecraft
play phase. Generic packet, codec, peer, sender, and registrar interfaces have
no Minecraft dependency, so Forge-family implementations can reuse them.
