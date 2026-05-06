package com.cmscomunidades.domain.model.valueobjects.erpclaimid;

public class EmptyERPClaimId extends ERPClaimId {

    public EmptyERPClaimId() {
        super(null);
    }

    @Override
    public String toString() {
        return "There is no ERP Claim ID associated with this claim.";
    }
}
