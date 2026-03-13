/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.mc21_11.wrappers

import dev.pandasystems.pandalib.wrappers.Identifier
import net.minecraft.resources.Identifier as McIdentifier

fun McIdentifier.toPl(): Identifier = IdentifierImpl(this)
fun Identifier.toMC(): McIdentifier {
    return if (this is IdentifierImpl) this.mcIdentifier
    else McIdentifier.fromNamespaceAndPath(this.namespace, this.path)
}

class IdentifierImpl(val mcIdentifier: McIdentifier): Identifier {
    override val namespace: String
        get() = mcIdentifier.namespace
    override val path: String
        get() = mcIdentifier.path
}