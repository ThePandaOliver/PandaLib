package dev.pandasystems.pandalib.core.handles

import net.minecraft.resources.Identifier as MCIdentifier

typealias Identifier = MCIdentifier

fun identifier(value: String): Identifier = MCIdentifier.parse(value)
fun identifier(namespace: String = "minecraft", value: String): Identifier = MCIdentifier.fromNamespaceAndPath(namespace, value)