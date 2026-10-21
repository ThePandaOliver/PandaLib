package dev.pandasystems.pandalib.core.handles

import net.minecraft.resources.Identifier as MCIdentifier

typealias Identifier = MCIdentifier

fun id(namespace: String = "minecraft", path: String): Identifier = MCIdentifier.fromNamespaceAndPath(namespace, path)
fun String.parseId(value: String): Identifier = MCIdentifier.parse(value)
