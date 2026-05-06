package com.cmscomunidades.domain.model.valueobjects.professional;

public enum Professional {
    NONE,
    CMS_ASISTENCIA,
    OTHER;

    public boolean equalsIgnoreCase(Professional other) {
        return this.name().equalsIgnoreCase(other.name());
    }
}