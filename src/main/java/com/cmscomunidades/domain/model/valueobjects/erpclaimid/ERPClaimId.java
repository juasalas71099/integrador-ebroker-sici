package com.cmscomunidades.domain.model.valueobjects.erpclaimid;

import java.util.Objects;

public class ERPClaimId {
    private final Long id;

    public ERPClaimId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ERPClaimId) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ERPClaimId[" +
                "id=" + id + ']';
    }

}