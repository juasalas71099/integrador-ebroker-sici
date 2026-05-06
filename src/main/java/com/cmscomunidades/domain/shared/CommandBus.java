package com.cmscomunidades.domain.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CommandBus {
    private final Map<Class<?>, Consumer<Object>> handlers = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void register(Class<T> commandType, Consumer<T> handler) {
        handlers.put(commandType, (Consumer<Object>) handler);
    }

    public void dispatch(Object command) {
        Consumer<Object> handler = handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No handler for command: " + command.getClass());
        }
        handler.accept(command);
    }
}

