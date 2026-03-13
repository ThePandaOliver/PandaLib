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

package dev.pandasystems.pandalib.mc21_11.wrappers.blocks

import dev.pandasystems.pandalib.wrappers.blocks.Block
import dev.pandasystems.pandalib.wrappers.blocks.BlockState
import net.minecraft.world.level.block.Block as McBlock

fun McBlock.toPl() = BlockImpl(this)
fun Block.toMC() =
    if (this is BlockImpl) this.mcBlock else throw IllegalArgumentException("Block is not an instance of BlockImpl")

class BlockImpl(val mcBlock: McBlock) : Block {
    override val defaultBlockState: BlockState by lazy { mcBlock.defaultBlockState().toPl() }
}