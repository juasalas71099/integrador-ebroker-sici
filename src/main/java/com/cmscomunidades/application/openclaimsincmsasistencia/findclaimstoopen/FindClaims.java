package com.cmscomunidades.application.openclaimsincmsasistencia.findclaimstoopen;

import com.cmscomunidades.application.openclaimsincmsasistencia.findclaimstoopen.filterclaims.FilterClaims;
import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.model.valueobjects.claimsbatch.ClaimsBatch;
import com.cmscomunidades.domain.port.erp.FindNewClaimsInERP;
import com.cmscomunidades.domain.port.repository.ClaimRepository;

public class FindClaims {

    private final FindNewClaimsInERP findNewClaimsInERP;
    private final ClaimRepository claimRepository;

    public FindClaims(FindNewClaimsInERP findNewClaimsInERP,
                      ClaimRepository claimRepository) {
        this.findNewClaimsInERP = findNewClaimsInERP;
        this.claimRepository = claimRepository;
    }

    public ClaimsBatch execute() {
        Claim lastOpenedClaim = findLastOpenedClaim();
        ClaimsBatch newClaimsInERP = findNewClaimsInERP(lastOpenedClaim);
        return filterClaimsToOpen(newClaimsInERP);
    }

    private Claim findLastOpenedClaim() {
        Claim lastOpenedClaim = new FindLastOpenedClaim(claimRepository).execute();
        System.out.println("Last opened claim: " + lastOpenedClaim);
        return lastOpenedClaim;
    }

    private ClaimsBatch findNewClaimsInERP(Claim lastOpenedClaim) {
        ClaimsBatch newClaimsInERP = findNewClaimsInERP.withIdGreaterThan(
                lastOpenedClaim.erpClaimId());
        System.out.println("Found " + newClaimsInERP.size() + " new claims in ERP.");
        return newClaimsInERP;
    }

    private ClaimsBatch filterClaimsToOpen(ClaimsBatch claimsBatch) {
        ClaimsBatch claimsToOpen = new FilterClaims().execute(claimsBatch);
        System.out.println("Filtered " + claimsToOpen.size() + " claims to open in CMS Asistencia.");
        return claimsToOpen;
    }
}
