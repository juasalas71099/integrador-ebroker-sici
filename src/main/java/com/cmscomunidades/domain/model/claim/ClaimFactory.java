package com.cmscomunidades.domain.model.claim;

import com.cmscomunidades.domain.model.valueobjects.claimid.EmptyClaimId;
import com.cmscomunidades.infrastructure.adapter.cmsasistencia.sendclaim.EmptySendClaim;

public class ClaimFactory {
    public static ClaimBuilder aClaimBuilder() {
        return new ClaimBuilder()
                .withClaimId(new EmptyClaimId())
                .withSendClaim(new EmptySendClaim());
    }
}