package com.cmscomunidades.domain.model.valueobjects.claimid;

public class EmptyClaimId extends ClaimId {

    public EmptyClaimId() {
        super(-1L);
    }

    @Override
    public String toString() {
        return "NoneClaimId{}";
    }
}
