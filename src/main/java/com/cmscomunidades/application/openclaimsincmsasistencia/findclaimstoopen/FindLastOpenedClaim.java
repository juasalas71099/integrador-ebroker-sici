package com.cmscomunidades.application.openclaimsincmsasistencia.findclaimstoopen;

import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.model.claim.ClaimBuilder;
import com.cmscomunidades.domain.model.claim.EmptyClaim;
import com.cmscomunidades.domain.port.repository.ClaimRepository;
import com.cmscomunidades.infrastructure.adapter.repository.ClaimBD;

import java.util.Optional;

public class FindLastOpenedClaim {

    private final ClaimRepository claimRepository;

    public FindLastOpenedClaim(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public Claim execute() {
        return findLastOpenedClaimInRepository()
                .map(this::toClaim)
                .orElse(new EmptyClaim());
    }

    private Optional<ClaimBD> findLastOpenedClaimInRepository() {
        return claimRepository.findTopByOrderByIdDesc();
    }

    private Claim toClaim(ClaimBD claimBD) {
        //todo map ClaimBD to Claim
        return new ClaimBuilder().build();
    }



}
