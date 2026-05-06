package com.cmscomunidades.application.openclaimsincmsasistencia.findclaimstoopen.filterclaims;

import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.model.valueobjects.claimsbatch.ClaimsBatch;
import com.cmscomunidades.domain.shared.Specification;

public class FilterClaims {
    public ClaimsBatch execute(ClaimsBatch claimsBatch) {
        Specification<Claim> cmsAsistencia = CMSAsistenciaSpecificationFactory.create();
        return claimsBatch.filterBySpecification(cmsAsistencia);
    }
}
