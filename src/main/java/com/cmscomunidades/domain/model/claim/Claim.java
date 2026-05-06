package com.cmscomunidades.domain.model.claim;

import com.cmscomunidades.domain.model.valueobjects.claimid.ClaimId;
import com.cmscomunidades.domain.model.valueobjects.claimid.EmptyClaimId;
import com.cmscomunidades.domain.model.valueobjects.erpclaimid.ERPClaimId;
import com.cmscomunidades.domain.model.valueobjects.erpclaimid.EmptyERPClaimId;
import com.cmscomunidades.domain.model.valueobjects.professional.Professional;
import com.cmscomunidades.domain.model.valueobjects.status.Status;
import com.cmscomunidades.domain.port.cmsasistencia.SendClaim;
import com.cmscomunidades.infrastructure.adapter.cmsasistencia.sendclaim.EmptySendClaim;


import java.util.Objects;

public class Claim {

    private ClaimId claimId;
    private ERPClaimId erpClaimId;
    private SendClaim sendClaim;
    private Professional professional;
    private Status status;

    Claim() {
        this.claimId = new EmptyClaimId();
        this.erpClaimId = new EmptyERPClaimId();
        this.sendClaim = new EmptySendClaim();
        this.professional = Professional.NONE;
        this.status = Status.NONE;
    }

    Claim(ClaimId claimId, ERPClaimId erpClaimId, SendClaim sendClaim,
                 Professional professional, Status status) {
        this.claimId = Objects.requireNonNull(claimId, "ClaimId cannot be null");
        this.sendClaim = Objects.requireNonNull(sendClaim, "SendClaim cannot be null");
        this.erpClaimId = Objects.requireNonNull(erpClaimId, "ERPClaimId cannot be null");
        this.professional = Objects.requireNonNull(professional, "Professional cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Claim other)) return false;
        return claimId.equals(other.claimId);
    }

    @Override
    public int hashCode() {
        return claimId.hashCode();
    }

    @Override
    public String toString() {
        return "Claim{" +
                "claimId=" + claimId +
                ", ERPClaimId=" + erpClaimId +
                ", sendClaim=" + sendClaim +
                ", professional=" + professional +
                ", status=" + status +
                '}';
    }

    public ERPClaimId erpClaimId() {
        return erpClaimId;
    }

    public Professional professional() {
        return this.professional;
    }

    public Status status() {
        return this.status;
    }

    public void openClaimInCMSAsistencia() {
        System.out.println("Opening claim in CMS Asistencia: " + claimId);
        System.out.println("Send Claim: " + sendClaim);
        sendClaim.send(null, this);
    }
}
