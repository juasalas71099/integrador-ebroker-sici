package com.cmscomunidades.domain.model.valueobjects.status;

import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.shared.Specification;

public class StatusSpecification implements Specification<Claim> {
    private final Status expectedStatus;

    public StatusSpecification(Status expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    @Override
    public boolean isSatisfiedBy(Claim claim) {
        return expectedStatus.equalsIgnoreCase(claim.status());
    }
}