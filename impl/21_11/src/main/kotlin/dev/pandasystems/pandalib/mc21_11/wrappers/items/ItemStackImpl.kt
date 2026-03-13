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

import dev.pandasystems.pandalib.wrappers.items.Item
import dev.pandasystems.pandalib.wrappers.items.ItemStack
import net.minecraft.world.item.ItemStack as McItemStack

fun McItemStack.toPl(): ItemStack = ItemStackImpl(this)
fun ItemStack.toMC(): McItemStack = if (this is ItemStackImpl) this.mcItemStack else throw IllegalArgumentException("ItemStack is not an instance of ItemStackImpl")

class ItemStackImpl(val mcItemStack: McItemStack) : ItemStack {
    override val item: Item by lazy { mcItemStack.item.toPl() }
    override var count: Int
        get() = mcItemStack.count
        set(value) {
            mcItemStack.count = value
        }
    override var durability: Int
        get() = mcItemStack.damageValue
        set(value) {
            mcItemStack.damageValue = value
        }
}