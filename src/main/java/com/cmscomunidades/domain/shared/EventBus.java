package com.cmscomunidades.domain.shared;

import java.util.*;
import java.util.function.Consumer;

public class EventBus {
    private final Map<Class<?>, List<Consumer<Object>>> handlers = new HashMap<>();

    public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add((Consumer<Object>) handler);
    }

    public void publish(Object event) {
        handlers.getOrDefault(event.getClass(), List.of())
                .forEach(handler -> handler.accept(event));
    }
}

