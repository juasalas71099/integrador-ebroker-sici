package com.cmscomunidades.domain.model.valueobjects.claimid;

import java.util.Objects;

public class ClaimId {
    private final long value;

    public ClaimId(long value) {
        this.value = value;
    }

    public static ClaimId of(long value) {
        return new ClaimId(value);
    }

    public long value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ClaimId) obj;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ClaimId[" +
                "value=" + value + ']';
    }

}

