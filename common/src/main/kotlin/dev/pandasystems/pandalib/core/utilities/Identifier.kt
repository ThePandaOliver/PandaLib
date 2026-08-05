package dev.pandasystems.pandalib.core.utilities

import net.minecraft.resources.Identifier as MCIdentifier

typealias Identifier = MCIdentifier

fun identifier(value: String): Identifier = MCIdentifier.parse(value)
fun identifier(value: String, namespace: String = "minecraft"): Identifier = MCIdentifier.fromNamespaceAndPath(namespace, value)