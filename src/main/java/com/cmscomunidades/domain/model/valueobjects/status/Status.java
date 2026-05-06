package com.cmscomunidades.domain.model.valueobjects.status;

public enum Status {
    NONE,
    OPEN,
    CANCELLED,
    CLOSED;

    public boolean equalsIgnoreCase(Status other) {
        return this.name().equalsIgnoreCase(other.name());
    }
}