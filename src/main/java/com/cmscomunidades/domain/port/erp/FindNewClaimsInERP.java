package com.cmscomunidades.domain.port.erp;

import com.cmscomunidades.domain.model.valueobjects.claimsbatch.ClaimsBatch;
import com.cmscomunidades.domain.model.valueobjects.erpclaimid.ERPClaimId;

public interface FindNewClaimsInERP {
    ClaimsBatch withIdGreaterThan(ERPClaimId erpClaimId);
}
