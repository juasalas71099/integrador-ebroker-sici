package com.cmscomunidades.domain.model.valueobjects.claimsbatch;

import com.cmscomunidades.domain.model.claim.Claim;

import java.util.List;

public class ClaimsBatchFactory {
    public ClaimsBatch of(List<Claim> claims) {
        return new ClaimsBatch(claims);
    }
}
