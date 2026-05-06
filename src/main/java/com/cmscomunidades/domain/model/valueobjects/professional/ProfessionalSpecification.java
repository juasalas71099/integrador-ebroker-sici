package com.cmscomunidades.domain.model.valueobjects.professional;

import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.shared.Specification;

public class ProfessionalSpecification implements Specification<Claim> {
    private final Professional expectedProfessional;

    public ProfessionalSpecification(Professional expectedProfessional) {
        this.expectedProfessional = expectedProfessional;
    }

    @Override
    public boolean isSatisfiedBy(Claim claim) {
        return expectedProfessional.equalsIgnoreCase(claim.professional());
    }
}