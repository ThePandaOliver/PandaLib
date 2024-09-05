/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.utils.codecs;

import net.minecraft.network.codec.StreamEncoder;

@FunctionalInterface
#if MC_VER >= MC_1_20_5
public interface PLStreamEncoder<O, T> extends StreamEncoder<O, T> {}
#else
public interface PLStreamEncoder<O, T> {
    void encode(O object, T object2);
}
#endif