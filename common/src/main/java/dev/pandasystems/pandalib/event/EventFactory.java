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

package dev.pandasystems.pandalib.event;

import com.google.common.reflect.AbstractInvocationHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class EventFactory {
	public static <T> Event<T> create() {
		return new EventImpl<T>(listeners -> new AbstractInvocationHandler() {
			@Override
			protected @Nullable Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
				for (T listener : listeners) {
					MethodHandles.lookup().unreflect(method).bindTo(listener).invokeWithArguments(args);
				}
				return null;
			}
		});
	}

	public static <T> Event<T> createCancellable() {
		return new EventImpl<T>(listeners -> new AbstractInvocationHandler() {
			@Override
			protected @NotNull Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
				for (T listener : listeners) {
					boolean result = (boolean) MethodHandles.lookup().unreflect(method).bindTo(listener).invokeWithArguments(args);
					if (result)
						return true;
				}
				return false;
			}
		});
	}

	private static class EventImpl<T> implements Event<T> {
		private final List<T> listeners = new ArrayList<>();
		private final Function<List<T>, InvocationHandler> invokerFunc;

		private EventImpl(Function<List<T>, InvocationHandler> invokerFunc) {
			this.invokerFunc = invokerFunc;
		}

		@Override
		@SuppressWarnings("unchecked")
		public T invoker() {
			return (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[] {listeners.getClass().getComponentType()}, invokerFunc.apply(listeners));
		}

		@Override
		public void register(T listener) {
			listeners.add(listener);
		}

		@Override
		public void unregister(T listener) {
			listeners.remove(listener);
		}
	}
}