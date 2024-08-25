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

package me.pandamods.pandalib.event;

import com.google.common.reflect.AbstractInvocationHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Borrowed from Architecrtury API
 */
public final class EventFactory {
    public static <T> Event<T> of(Function<List<T>, T> function) {
        return new EventImpl<>(function);
    }

	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <T> Event<T> createLoop(T... typeGetter) {
		if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
		return createLoop((Class<T>) typeGetter.getClass().getComponentType());
	}

	@SuppressWarnings("unchecked")
	private static <T, R> R invokeMethod(T listener, Method method, Object[] args) throws Throwable {
		return (R) MethodHandles.lookup().unreflect(method)
				.bindTo(listener).invokeWithArguments(args);
	}

	@SuppressWarnings("unchecked")
	public static <T> Event<T> createLoop(Class<T> clazz) {
		return of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
			@Override
			protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
				for (var listener : listeners) {
					invokeMethod(listener, method, args);
				}
				return null;
			}
		}));
	}

    private static class EventImpl<T> implements Event<T> {
        private final Function<List<T>, T> function;
        private T invoker = null;
        private ArrayList<T> listeners;
        
        public EventImpl(Function<List<T>, T> function) {
            this.function = function;
            this.listeners = new ArrayList<>();
        }
        
        @Override
        public T invoker() {
            if (invoker == null) {
                update();
            }
            return invoker;
        }
        
        @Override
        public void register(T listener) {
            listeners.add(listener);
            invoker = null;
        }
        
        @Override
        public void unregister(T listener) {
            listeners.remove(listener);
            listeners.trimToSize();
            invoker = null;
        }
        
        @Override
        public boolean isRegistered(T listener) {
            return listeners.contains(listener);
        }
        
        @Override
        public void clearListeners() {
            listeners.clear();
            listeners.trimToSize();
            invoker = null;
        }
        
        public void update() {
            if (listeners.size() == 1) {
                invoker = listeners.get(0);
            } else {
                invoker = function.apply(listeners);
            }
        }
    }
}