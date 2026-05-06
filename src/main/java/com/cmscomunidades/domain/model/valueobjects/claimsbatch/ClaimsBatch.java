package com.cmscomunidades.domain.model.valueobjects.claimsbatch;

import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.shared.Specification;

import java.util.List;

public class ClaimsBatch {
    private final List<Claim> claims;

    ClaimsBatch() {
        this.claims = List.of();
    }

    ClaimsBatch(List<Claim> claims) {
        this.claims = claims;
    }

    private ClaimsBatch toClaimsBatch(List<Claim> filteredClaims) {
        if (filteredClaims.isEmpty()) {
            return new EmptyClaimsBatch();
        }
        return new ClaimsBatchFactory().of(filteredClaims);
    }

    public ClaimsBatch filterBySpecification(Specification<Claim> specification) {
        List<Claim> filteredClaims = claims.stream()
                .filter(specification::isSatisfiedBy)
                .toList();
        return toClaimsBatch(filteredClaims);
    }

    public void openClaimsInCMSAsistencia() {
        claims.forEach(Claim::openClaimInCMSAsistencia);
    }

    public String size() {
        return String.valueOf(claims.size());
    }

    @Override
    public String toString() {
        return "ClaimsBatch{" +
                "claims=" + claims +
                '}';
    }
}
