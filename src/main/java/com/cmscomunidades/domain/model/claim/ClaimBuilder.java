package com.cmscomunidades.domain.model.claim;

import com.cmscomunidades.domain.model.valueobjects.erpclaimid.ERPClaimId;
import com.cmscomunidades.domain.model.valueobjects.claimid.ClaimId;
import com.cmscomunidades.domain.model.valueobjects.erpclaimid.EmptyERPClaimId;
import com.cmscomunidades.domain.model.valueobjects.professional.Professional;
import com.cmscomunidades.domain.model.valueobjects.status.Status;
import com.cmscomunidades.domain.port.cmsasistencia.SendClaim;

public class ClaimBuilder {
    private ClaimId claimId;
    private SendClaim sendClaim;
    private ERPClaimId erpClaimId;
    private Professional professional;
    private Status status;

    public ClaimBuilder withClaimId(ClaimId claimId) {
        this.claimId = claimId;
        return this;
    }

    public ClaimBuilder withSendClaim(SendClaim sendClaim) {
        this.sendClaim = sendClaim;
        return this;
    }

    public ClaimBuilder withEBrokerId(ERPClaimId ERPClaimId) {
        this.erpClaimId = ERPClaimId;
        return this;
    }

    public ClaimBuilder withProfessional(Professional professional) {
        this.professional = professional;
        return this;
    }

    public ClaimBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public Claim build() {
        if (erpClaimId == null) {
            erpClaimId = new EmptyERPClaimId();
        }

        if (professional == null) {
            professional = Professional.NONE;
        }

        return new Claim(claimId, erpClaimId, sendClaim, professional, status);
    }
}
