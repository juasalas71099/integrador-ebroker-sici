package com.cmscomunidades.application.openclaimsincmsasistencia;

import com.cmscomunidades.application.openclaimsincmsasistencia.findclaimstoopen.FindClaims;
import com.cmscomunidades.application.openclaimsincmsasistencia.event.ClaimsToOpenFound;
import com.cmscomunidades.application.openclaimsincmsasistencia.listener.ClaimsToOpenFoundListener;
import com.cmscomunidades.domain.model.valueobjects.claimsbatch.ClaimsBatch;
import com.cmscomunidades.domain.port.erp.FindNewClaimsInERP;
import com.cmscomunidades.domain.port.repository.ClaimRepository;
import com.cmscomunidades.domain.shared.EventBus;

public class OpenClaims {

    private final FindNewClaimsInERP findNewClaimsInERP;
    private final ClaimRepository claimRepository;
    private EventBus eventBus;

    public OpenClaims(FindNewClaimsInERP findNewClaimsInERP,
                      ClaimRepository claimRepository) {
        this.findNewClaimsInERP = findNewClaimsInERP;
        this.claimRepository = claimRepository;

        tempMain();
    }

    private void tempMain() {
        this.eventBus = new EventBus();
        ClaimsToOpenFoundListener claimsToOpenFoundListener = new ClaimsToOpenFoundListener();
        this.eventBus.subscribe(ClaimsToOpenFound.class, claimsToOpenFoundListener::onEvent);
    }

    public void execute() {
        ClaimsBatch claimsToOpen = findClaimsToOpen();
        eventBus.publish(new ClaimsToOpenFound(claimsToOpen));
    }

    private ClaimsBatch findClaimsToOpen() {
        return new FindClaims(findNewClaimsInERP, claimRepository).execute();
    }
}

//    public void execute() {
//        ClaimsBatch claimsToOpen = findClaimsToOpen();
//        System.out.println("Found " + claimsToOpen.size() + " claims to open in CMS Asistencia.");
//        claimsToOpen.openClaimsInCMSAsistencia();
//    }