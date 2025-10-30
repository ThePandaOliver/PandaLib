/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package dev.pandasystems.pandalib.utils.codecs

import com.google.common.base.Suppliers
import com.mojang.datafixers.util.*
import io.netty.buffer.ByteBuf
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

interface StreamCodec<B, V> : StreamDecoder<B, V>, StreamEncoder<B, V> {
	fun <O> apply(operation: CodecOperation<B, V, O>): StreamCodec<B, O> {
		return operation.apply(this)
	}

	fun <O> map(factory: Function<in V, out O>, getter: Function<in O, out V>): StreamCodec<B, O> {
		return object : StreamCodec<B, O> {
			override fun decode(obj: B): O {
				return factory.apply(this@StreamCodec.decode(obj))
			}

			override fun encode(obj: B, obj2: O) {
				this@StreamCodec.encode(obj, getter.apply(obj2))
			}
		}
	}

	fun <O : ByteBuf> mapStream(bufferFactory: Function<O, out B>): StreamCodec<O, V> {
		return object : StreamCodec<O, V> {
			override fun decode(obj: O): V {
				val obj = bufferFactory.apply(obj)
				return this@StreamCodec.decode(obj)
			}

			override fun encode(obj: O, obj2: V) {
				val obj3 = bufferFactory.apply(obj)
				this@StreamCodec.encode(obj3, obj2)
			}
		}
	}

	fun <U> dispatch(keyGetter: Function<in U, out V>, codecGetter: Function<in V, out StreamCodec<in B, out U>>): StreamCodec<B, U> {
		return object : StreamCodec<B, U> {
			override fun decode(obj: B): U {
				val obj2 = this@StreamCodec.decode(obj)
				val streamCodec: StreamCodec<in B, out U> = codecGetter.apply(obj2)
				return streamCodec.decode(obj)
			}

			override fun encode(obj: B, obj2: U) {
				val obj3 = keyGetter.apply(obj2)
				@Suppress("UNCHECKED_CAST")
				val streamCodec: StreamCodec<B, U> = codecGetter.apply(obj3) as StreamCodec<B, U>
				this@StreamCodec.encode(obj, obj3)
				streamCodec.encode(obj, obj2)
			}
		}
	}

	fun <S : B> cast(): StreamCodec<S, V> {
		@Suppress("UNCHECKED_CAST")
		return this as StreamCodec<S, V>
	}

	fun interface CodecOperation<B, S, T> {
		fun apply(streamCodec: StreamCodec<B, S>): StreamCodec<B, T>
	}

	companion object {
		fun <B, V> of(encoder: StreamEncoder<B, V>, decoder: StreamDecoder<B, V>): StreamCodec<B, V> {
			return object : StreamCodec<B, V> {
				override fun decode(obj: B): V {
					return decoder.decode(obj)
				}

				override fun encode(obj: B, obj2: V) {
					encoder.encode(obj, obj2)
				}
			}
		}

		fun <B, V> ofMember(encoder: StreamMemberEncoder<B, V>, decoder: StreamDecoder<B, V>): StreamCodec<B, V> {
			return object : StreamCodec<B, V> {
				override fun decode(obj: B): V {
					return decoder.decode(obj)
				}

				override fun encode(obj: B, obj2: V) {
					encoder.encode(obj2, obj)
				}
			}
		}

		fun <B, V> unit(expectedValue: V): StreamCodec<B, V> {
			return object : StreamCodec<B, V> {
				override fun decode(obj: B): V {
					return expectedValue
				}

				override fun encode(obj: B, obj2: V) {
					if (obj2 != expectedValue) {
						throw IllegalStateException("Can't encode '$obj2', expected '$expectedValue'")
					}
				}
			}
		}

		fun <B, C, T1> composite(
			codec: StreamCodec<in B, T1>, getter: Function<C, T1>,
			factory: Function<T1, C>
		): StreamCodec<B, C> {
			return object : StreamCodec<B, C> {
				override fun decode(obj: B): C {
					val object2 = codec.decode(obj)
					return factory.apply(object2)
				}

				override fun encode(obj: B, obj2: C) {
					codec.encode(obj, getter.apply(obj2))
				}
			}
		}

		fun <B, C, T1, T2> composite(
			codec1: StreamCodec<in B, T1>,
			getter1: Function<C, T1>,
			codec2: StreamCodec<in B, T2>,
			getter2: Function<C, T2>,
			factory: BiFunction<T1, T2, C>
		): StreamCodec<B, C> {
			return object : StreamCodec<B, C> {
				override fun decode(obj: B): C {
					val object2 = codec1.decode(obj)
					val object3 = codec2.decode(obj)
					return factory.apply(object2, object3)
				}

				override fun encode(obj: B, obj2: C) {
					codec1.encode(obj, getter1.apply(obj2))
					codec2.encode(obj, getter2.apply(obj2))
				}
			}
		}

		fun <B, C, T1, T2, T3> composite(
			codec1: StreamCodec<in B, T1>,
			getter1: Function<C, T1>,
			codec2: StreamCodec<in B, T2>,
			getter2: Function<C, T2>,
			codec3: StreamCodec<in B, T3>,
			getter3: Function<C, T3>,
			factory: Function3<T1, T2, T3, C>
		): StreamCodec<B, C> {
			return object : StreamCodec<B, C> {
				override fun decode(obj: B): C {
					val object2 = codec1.decode(obj)
					val object3 = codec2.decode(obj)
					val object4 = codec3.decode(obj)
					return factory.apply(object2, object3, object4)
				}

				override fun encode(obj: B, obj2: C) {
					codec1.encode(obj, getter1.apply(obj2))
					codec2.encode(obj, getter2.apply(obj2))
					codec3.encode(obj, getter3.apply(obj2))
				}
			}
		}

		fun <B, C, T1, T2, T3, T4> composite(
			codec1: StreamCodec<in B, T1>,
			getter1: Function<C, T1>,
			codec2: StreamCodec<in B, T2>,
			getter2: Function<C, T2>,
			codec3: StreamCodec<in B, T3>,
			getter3: Function<C, T3>,
			codec4: StreamCodec<in B, T4>,
			getter4: Function<C, T4>,
			factory: Function4<T1, T2, T3, T4, C>
		): StreamCodec<B, C> {
			return object : StreamCodec<B, C> {
				override fun decode(obj: B): C {
					val object2 = codec1.decode(obj)
					val object3 = codec2.decode(obj)
					val object4 = codec3.decode(obj)
					val object5 = codec4.decode(obj)
					return factory.apply(object2, object3, object4, object5)
				}

				override fun encode(obj: B, obj2: C) {
					codec1.encode(obj, getter1.apply(obj2))
					codec2.encode(obj, getter2.apply(obj2))
					codec3.encode(obj, getter3.apply(obj2))
					codec4.encode(obj, getter4.apply(obj2))
				}
			}
		}

		fun <B, C, T1, T2, T3, T4, T5> composite(
			codec1: StreamCodec<in B, T1>,
			getter1: Function<C, T1>,
			codec2: StreamCodec<in B, T2>,
			getter2: Function<C, T2>,
			codec3: StreamCodec<in B, T3>,
			getter3: Function<C, T3>,
			codec4: StreamCodec<in B, T4>,
			getter4: Function<C, T4>,
			codec5: StreamCodec<in B, T5>,
			getter5: Function<C, T5>,
			factory: Function5<T1, T2, T3, T4, T5, C>
		): StreamCodec<B, C> {
			return object : StreamCodec<B, C> {
				override fun decode(obj: B): C {
					val object2 = codec1.decode(obj)
					val object3 = codec2.decode(obj)
					val object4 = codec3.decode(obj)
					val object5 = codec4.decode(obj)
					val object6 = codec5.decode(obj)
					return factory.apply(object2, object3, object4, object5, object6)
				}

				override fun encode(obj: B, obj2: C) {
					codec1.encode(obj, getter1.apply(obj2))
					codec2.encode(obj, getter2.apply(obj2))
					codec3.encode(obj, getter3.apply(obj2))
					codec4.encode(obj, getter4.apply(obj2))
					codec5.encode(obj, getter5.apply(obj2))
				}
			}
		}

		fun <B, C, T1, T2, T3, T4, T5, T6> composite(
			codec1: StreamCodec<in B, T1>,
			getter1: Function<C, T1>,
			codec2: StreamCodec<in B, T2>,
			getter2: Function<C, T2>,
			codec3: StreamCodec<in B, T3>,
			getter3: Function<C, T3>,
			codec4: StreamCodec<in B, T4>,
			getter4: Function<C, T4>,
			codec5: StreamCodec<in B, T5>,
			getter5: Function<C, T5>,
			codec6: StreamCodec<in B, T6>,
			getter6: Function<C, T6>,
			factory: Function6<T1, T2, T3, T4, T5, T6, C>
		): StreamCodec<B, C> {
			return object : StreamCodec<B, C> {
				override fun decode(obj: B): C {
					val object2 = codec1.decode(obj)
					val object3 = codec2.decode(obj)
					val object4 = codec3.decode(obj)
					val object5 = codec4.decode(obj)
					val object6 = codec5.decode(obj)
					val object7 = codec6.decode(obj)
					return factory.apply(object2, object3, object4, object5, object6, object7)
				}

				override fun encode(obj: B, obj2: C) {
					codec1.encode(obj, getter1.apply(obj2))
					codec2.encode(obj, getter2.apply(obj2))
					codec3.encode(obj, getter3.apply(obj2))
					codec4.encode(obj, getter4.apply(obj2))
					codec5.encode(obj, getter5.apply(obj2))
					codec6.encode(obj, getter6.apply(obj2))
				}
			}
		}

		fun <B, C, T1, T2, T3, T4, T5, T6, T7> composite(
			codec1: StreamCodec<in B, T1>,
			getter1: Function<C, T1>,
			codec2: StreamCodec<in B, T2>,
			getter2: Function<C, T2>,
			codec3: StreamCodec<in B, T3>,
			getter3: Function<C, T3>,
			codec4: StreamCodec<in B, T4>,
			getter4: Function<C, T4>,
			codec5: StreamCodec<in B, T5>,
			getter5: Function<C, T5>,
			codec6: StreamCodec<in B, T6>,
			getter6: Function<C, T6>,
			codec7: StreamCodec<in B, T7>,
			getter7: Function<C, T7>,
			factory: Function7<T1, T2, T3, T4, T5, T6, T7, C>
		): StreamCodec<B, C> {
			return object : StreamCodec<B, C> {
				override fun decode(obj: B): C {
					val object2 = codec1.decode(obj)
					val object3 = codec2.decode(obj)
					val object4 = codec3.decode(obj)
					val object5 = codec4.decode(obj)
					val object6 = codec5.decode(obj)
					val object7 = codec6.decode(obj)
					val object8 = codec7.decode(obj)
					return factory.apply(object2, object3, object4, object5, object6, object7, object8)
				}

				override fun encode(obj: B, obj2: C) {
					codec1.encode(obj, getter1.apply(obj2))
					codec2.encode(obj, getter2.apply(obj2))
					codec3.encode(obj, getter3.apply(obj2))
					codec4.encode(obj, getter4.apply(obj2))
					codec5.encode(obj, getter5.apply(obj2))
					codec6.encode(obj, getter6.apply(obj2))
					codec7.encode(obj, getter7.apply(obj2))
				}
			}
		}

		fun <B, C, T1, T2, T3, T4, T5, T6, T7, T8> composite(
			codec1: StreamCodec<in B, T1>,
			getter1: Function<C, T1>,
			codec2: StreamCodec<in B, T2>,
			getter2: Function<C, T2>,
			codec3: StreamCodec<in B, T3>,
			getter3: Function<C, T3>,
			codec4: StreamCodec<in B, T4>,
			getter4: Function<C, T4>,
			codec5: StreamCodec<in B, T5>,
			getter5: Function<C, T5>,
			codec6: StreamCodec<in B, T6>,
			getter6: Function<C, T6>,
			codec7: StreamCodec<in B, T7>,
			getter7: Function<C, T7>,
			codec8: StreamCodec<in B, T8>,
			getter8: Function<C, T8>,
			factory: Function8<T1, T2, T3, T4, T5, T6, T7, T8, C>
		): StreamCodec<B, C> {
			return object : StreamCodec<B, C> {
				override fun decode(obj: B): C {
					val object2 = codec1.decode(obj)
					val object3 = codec2.decode(obj)
					val object4 = codec3.decode(obj)
					val object5 = codec4.decode(obj)
					val object6 = codec5.decode(obj)
					val object7 = codec6.decode(obj)
					val object8 = codec7.decode(obj)
					val object9 = codec8.decode(obj)
					return factory.apply(object2, object3, object4, object5, object6, object7, object8, object9)
				}

				override fun encode(obj: B, obj2: C) {
					codec1.encode(obj, getter1.apply(obj2))
					codec2.encode(obj, getter2.apply(obj2))
					codec3.encode(obj, getter3.apply(obj2))
					codec4.encode(obj, getter4.apply(obj2))
					codec5.encode(obj, getter5.apply(obj2))
					codec6.encode(obj, getter6.apply(obj2))
					codec7.encode(obj, getter7.apply(obj2))
					codec8.encode(obj, getter8.apply(obj2))
				}
			}
		}

		fun <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> composite(
			codec1: StreamCodec<in B, T1>,
			getter1: Function<C, T1>,
			codec2: StreamCodec<in B, T2>,
			getter2: Function<C, T2>,
			codec3: StreamCodec<in B, T3>,
			getter3: Function<C, T3>,
			codec4: StreamCodec<in B, T4>,
			getter4: Function<C, T4>,
			codec5: StreamCodec<in B, T5>,
			getter5: Function<C, T5>,
			codec6: StreamCodec<in B, T6>,
			getter6: Function<C, T6>,
			codec7: StreamCodec<in B, T7>,
			getter7: Function<C, T7>,
			codec8: StreamCodec<in B, T8>,
			getter8: Function<C, T8>,
			codec9: StreamCodec<in B, T9>,
			getter9: Function<C, T9>,
			factory: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C>
		): StreamCodec<B, C> {
			return object : StreamCodec<B, C> {
				override fun decode(obj: B): C {
					val object2 = codec1.decode(obj)
					val object3 = codec2.decode(obj)
					val object4 = codec3.decode(obj)
					val object5 = codec4.decode(obj)
					val object6 = codec5.decode(obj)
					val object7 = codec6.decode(obj)
					val object8 = codec7.decode(obj)
					val object9 = codec8.decode(obj)
					val object10 = codec9.decode(obj)
					return factory.apply(object2, object3, object4, object5, object6, object7, object8, object9, object10)
				}

				override fun encode(obj: B, obj2: C) {
					codec1.encode(obj, getter1.apply(obj2))
					codec2.encode(obj, getter2.apply(obj2))
					codec3.encode(obj, getter3.apply(obj2))
					codec4.encode(obj, getter4.apply(obj2))
					codec5.encode(obj, getter5.apply(obj2))
					codec6.encode(obj, getter6.apply(obj2))
					codec7.encode(obj, getter7.apply(obj2))
					codec8.encode(obj, getter8.apply(obj2))
					codec9.encode(obj, getter9.apply(obj2))
				}
			}
		}

		fun <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> composite(
			codec1: StreamCodec<in B, T1>,
			getter1: Function<C, T1>,
			codec2: StreamCodec<in B, T2>,
			getter2: Function<C, T2>,
			codec3: StreamCodec<in B, T3>,
			getter3: Function<C, T3>,
			codec4: StreamCodec<in B, T4>,
			getter4: Function<C, T4>,
			codec5: StreamCodec<in B, T5>,
			getter5: Function<C, T5>,
			codec6: StreamCodec<in B, T6>,
			getter6: Function<C, T6>,
			codec7: StreamCodec<in B, T7>,
			getter7: Function<C, T7>,
			codec8: StreamCodec<in B, T8>,
			getter8: Function<C, T8>,
			codec9: StreamCodec<in B, T9>,
			getter9: Function<C, T9>,
			codec10: StreamCodec<in B, T10>,
			getter10: Function<C, T10>,
			factory: Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C>
		): StreamCodec<B, C> {
			return object : StreamCodec<B, C> {
				override fun decode(obj: B): C {
					val object1 = codec1.decode(obj)
					val object2 = codec2.decode(obj)
					val object3 = codec3.decode(obj)
					val object4 = codec4.decode(obj)
					val object5 = codec5.decode(obj)
					val object6 = codec6.decode(obj)
					val object7 = codec7.decode(obj)
					val object8 = codec8.decode(obj)
					val object9 = codec9.decode(obj)
					val object10 = codec10.decode(obj)
					return factory.apply(object1, object2, object3, object4, object5, object6, object7, object8, object9, object10)
				}

				override fun encode(obj: B, obj2: C) {
					codec1.encode(obj, getter1.apply(obj2))
					codec2.encode(obj, getter2.apply(obj2))
					codec3.encode(obj, getter3.apply(obj2))
					codec4.encode(obj, getter4.apply(obj2))
					codec5.encode(obj, getter5.apply(obj2))
					codec6.encode(obj, getter6.apply(obj2))
					codec7.encode(obj, getter7.apply(obj2))
					codec8.encode(obj, getter8.apply(obj2))
					codec9.encode(obj, getter9.apply(obj2))
					codec10.encode(obj, getter10.apply(obj2))
				}
			}
		}

		fun <B, T> recursive(modifier: UnaryOperator<StreamCodec<B, T>>): StreamCodec<B, T> {
			return object : StreamCodec<B, T> {
				private val inner: Supplier<StreamCodec<B, T>> = Suppliers.memoize { modifier.apply(this) }

				override fun decode(obj: B): T {
					return inner.get().decode(obj)
				}

				override fun encode(obj: B, obj2: T) {
					inner.get().encode(obj, obj2)
				}
			}
		}
	}
}