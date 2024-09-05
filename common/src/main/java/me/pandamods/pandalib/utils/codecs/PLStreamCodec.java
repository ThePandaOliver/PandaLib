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

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;
import io.netty.buffer.ByteBuf;
#if MC_VER >= MC_1_20_5
import net.minecraft.network.codec.StreamCodec;
#endif

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

#if MC_VER >= MC_1_20_5
public interface PLStreamCodec<B, V> extends StreamCodec<B, V> { }
#else
public interface PLStreamCodec<B, V> extends PLStreamEncoder<B, V>, PLStreamDecoder<B, V> {
	static <B, V> PLStreamCodec<B, V> of(final PLStreamEncoder<B, V> encoder, final PLStreamDecoder<B, V> decoder) {
		return new PLStreamCodec<B, V>() {
			public V decode(B object) {
				return decoder.decode(object);
			}

			public void encode(B object, V object2) {
				encoder.encode(object, object2);
			}
		};
	}

	static <B, V> PLStreamCodec<B, V> ofMember(final PLStreamMemberEncoder<B, V> encoder, final PLStreamCodec<B, V> decoder) {
		return new PLStreamCodec<B, V>() {
			public V decode(B object) {
				return decoder.decode(object);
			}

			public void encode(B object, V object2) {
				encoder.encode(object2, object);
			}
		};
	}

	static <B, V> PLStreamCodec<B, V> unit(final V expectedValue) {
		return new PLStreamCodec<B, V>() {
			public V decode(B object) {
				return expectedValue;
			}

			public void encode(B object, V object2) {
				if (!object2.equals(expectedValue)) {
					String var10002 = String.valueOf(object2);
					throw new IllegalStateException("Can't encode '" + var10002 + "', expected '" + expectedValue + "'");
				}
			}
		};
	}

	default <O> PLStreamCodec<B, O> apply(PLStreamCodec.CodecOperation<B, V, O> operation) {
		return operation.apply(this);
	}

	default <O> PLStreamCodec<B, O> map(final Function<? super V, ? extends O> factory, final Function<? super O, ? extends V> getter) {
		return new PLStreamCodec<B, O>() {
			public O decode(B object) {
				return factory.apply(PLStreamCodec.this.decode(object));
			}

			public void encode(B object, O object2) {
				PLStreamCodec.this.encode(object, getter.apply(object2));
			}
		};
	}

	default <O extends ByteBuf> PLStreamCodec<O, V> mapStream(final Function<O, ? extends B> bufferFactory) {
		return new PLStreamCodec<O, V>() {
			public V decode(O byteBuf) {
				B object = bufferFactory.apply(byteBuf);
				return PLStreamCodec.this.decode(object);
			}

			public void encode(O byteBuf, V object) {
				B object2 = bufferFactory.apply(byteBuf);
				PLStreamCodec.this.encode(object2, object);
			}
		};
	}

	@SuppressWarnings("unchecked")
	default <U> PLStreamCodec<B, U> dispatch(final Function<? super U, ? extends V> keyGetter,
											 final Function<? super V, ? extends PLStreamCodec<? super B, ? extends U>> codecGetter) {
		return new PLStreamCodec<B, U>() {
			public U decode(B object) {
				V object2 = PLStreamCodec.this.decode(object);
				PLStreamCodec<? super B, ? extends U> streamCodec = (PLStreamCodec) codecGetter.apply(object2);
				return streamCodec.decode(object);
			}

			public void encode(B object, U object2) {
				V object3 = keyGetter.apply(object2);
				PLStreamCodec<B, U> streamCodec = (PLStreamCodec) codecGetter.apply(object3);
				PLStreamCodec.this.encode(object, object3);
				streamCodec.encode(object, object2);
			}
		};
	}

	static <B, C, T1> PLStreamCodec<B, C> composite(final PLStreamCodec<? super B, T1> codec, final Function<C, T1> getter,
													final Function<T1, C> factory) {
		return new PLStreamCodec<B, C>() {
			public C decode(B object) {
				T1 object2 = codec.decode(object);
				return factory.apply(object2);
			}

			public void encode(B object, C object2) {
				codec.encode(object, getter.apply(object2));
			}
		};
	}

	static <B, C, T1, T2> PLStreamCodec<B, C> composite(final PLStreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
														final PLStreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
														final BiFunction<T1, T2, C> factory) {
		return new PLStreamCodec<B, C>() {
			public C decode(B object) {
				T1 object2 = codec1.decode(object);
				T2 object3 = codec2.decode(object);
				return factory.apply(object2, object3);
			}

			public void encode(B object, C object2) {
				codec1.encode(object, getter1.apply(object2));
				codec2.encode(object, getter2.apply(object2));
			}
		};
	}

	static <B, C, T1, T2, T3> PLStreamCodec<B, C> composite(final PLStreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
															final PLStreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
															final PLStreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
															final Function3<T1, T2, T3, C> factory) {
		return new PLStreamCodec<B, C>() {
			public C decode(B object) {
				T1 object2 = codec1.decode(object);
				T2 object3 = codec2.decode(object);
				T3 object4 = codec3.decode(object);
				return factory.apply(object2, object3, object4);
			}

			public void encode(B object, C object2) {
				codec1.encode(object, getter1.apply(object2));
				codec2.encode(object, getter2.apply(object2));
				codec3.encode(object, getter3.apply(object2));
			}
		};
	}

	static <B, C, T1, T2, T3, T4> PLStreamCodec<B, C> composite(final PLStreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
																final PLStreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
																final PLStreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
																final PLStreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
																final Function4<T1, T2, T3, T4, C> factory) {
		return new PLStreamCodec<B, C>() {
			public C decode(B object) {
				T1 object2 = codec1.decode(object);
				T2 object3 = codec2.decode(object);
				T3 object4 = codec3.decode(object);
				T4 object5 = codec4.decode(object);
				return factory.apply(object2, object3, object4, object5);
			}

			public void encode(B object, C object2) {
				codec1.encode(object, getter1.apply(object2));
				codec2.encode(object, getter2.apply(object2));
				codec3.encode(object, getter3.apply(object2));
				codec4.encode(object, getter4.apply(object2));
			}
		};
	}

	static <B, C, T1, T2, T3, T4, T5> PLStreamCodec<B, C> composite(final PLStreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
																	final PLStreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
																	final PLStreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
																	final PLStreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
																	final PLStreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
																	final Function5<T1, T2, T3, T4, T5, C> factory) {
		return new PLStreamCodec<B, C>() {
			public C decode(B object) {
				T1 object2 = codec1.decode(object);
				T2 object3 = codec2.decode(object);
				T3 object4 = codec3.decode(object);
				T4 object5 = codec4.decode(object);
				T5 object6 = codec5.decode(object);
				return factory.apply(object2, object3, object4, object5, object6);
			}

			public void encode(B object, C object2) {
				codec1.encode(object, getter1.apply(object2));
				codec2.encode(object, getter2.apply(object2));
				codec3.encode(object, getter3.apply(object2));
				codec4.encode(object, getter4.apply(object2));
				codec5.encode(object, getter5.apply(object2));
			}
		};
	}

	static <B, C, T1, T2, T3, T4, T5, T6> PLStreamCodec<B, C> composite(final PLStreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
																		final PLStreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
																		final PLStreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
																		final PLStreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
																		final PLStreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
																		final PLStreamCodec<? super B, T6> codec6, final Function<C, T6> getter6,
																		final Function6<T1, T2, T3, T4, T5, T6, C> factory) {
		return new PLStreamCodec<B, C>() {
			public C decode(B object) {
				T1 object2 = codec1.decode(object);
				T2 object3 = codec2.decode(object);
				T3 object4 = codec3.decode(object);
				T4 object5 = codec4.decode(object);
				T5 object6 = codec5.decode(object);
				T6 object7 = codec6.decode(object);
				return factory.apply(object2, object3, object4, object5, object6, object7);
			}

			public void encode(B object, C object2) {
				codec1.encode(object, getter1.apply(object2));
				codec2.encode(object, getter2.apply(object2));
				codec3.encode(object, getter3.apply(object2));
				codec4.encode(object, getter4.apply(object2));
				codec5.encode(object, getter5.apply(object2));
				codec6.encode(object, getter6.apply(object2));
			}
		};
	}

	@SuppressWarnings("unchecked")
	static <B, T> PLStreamCodec<B, T> recursive(final UnaryOperator<PLStreamCodec<B, T>> modifier) {
		return new PLStreamCodec<B, T>() {
			private final Supplier<PLStreamCodec<B, T>> inner = Suppliers.memoize(() -> (PLStreamCodec) modifier.apply(this));

			public T decode(B object) {
				return (T) ((PLStreamCodec) this.inner.get()).decode(object);
			}

			public void encode(B object, T object2) {
				((PLStreamCodec) this.inner.get()).encode(object, object2);
			}
		};
	}

	@SuppressWarnings("unchecked")
	default <S extends B> PLStreamCodec<S, V> cast() {
		return (PLStreamCodec<S, V>) this;
	}

	@FunctionalInterface
	interface CodecOperation<B, S, T> {
		PLStreamCodec<B, T> apply(PLStreamCodec<B, S> streamCodec);
	}
}
#endif
