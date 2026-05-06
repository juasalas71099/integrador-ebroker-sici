package com.cmscomunidades.application.openclaimsincmsasistencia.event;

import com.cmscomunidades.domain.model.valueobjects.claimsbatch.ClaimsBatch;
import com.cmscomunidades.domain.shared.Event;

public class ClaimsToOpenFound implements Event {
    private final ClaimsBatch claimsBatch;

    public ClaimsToOpenFound(ClaimsBatch claimsBatch) {
        this.claimsBatch = claimsBatch;
    }

    public ClaimsBatch claimsBatch() {
        return claimsBatch;
    }
}
