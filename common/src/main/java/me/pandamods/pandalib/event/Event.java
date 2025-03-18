package me.pandamods.pandalib.event;

public interface Event<T> {
    T invoker();
    void register(T listener);
    void unregister(T listener);
}