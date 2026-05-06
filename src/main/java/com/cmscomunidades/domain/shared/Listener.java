package com.cmscomunidades.domain.shared;

public interface Listener<T> {
    void onEvent(T event);
}