package dev.pandasystems.pandalib.core.utils.extensions

import net.minecraft.resources.ResourceLocation

fun resourceLocation(namespace: String, path: String): ResourceLocation =
	ResourceLocation.fromNamespaceAndPath(namespace, path)