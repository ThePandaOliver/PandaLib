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

package dev.pandasystems.pandalib.mc21_11.wrappers.items

import dev.pandasystems.pandalib.mc21_11.wrappers.toPl
import dev.pandasystems.pandalib.wrappers.Identifier
import dev.pandasystems.pandalib.wrappers.items.Item
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item as McItem

fun McItem.toPl(): Item = ItemImpl(this)
fun Item.toMC(): McItem {
    return if (this is ItemImpl) this.mcItem
    else throw IllegalArgumentException("Item is not an instance of ItemImpl")
}

class ItemImpl(val mcItem: McItem): Item {
    override val identifier: Identifier
        get() = BuiltInRegistries.ITEM.getKey(mcItem).toPl()
}