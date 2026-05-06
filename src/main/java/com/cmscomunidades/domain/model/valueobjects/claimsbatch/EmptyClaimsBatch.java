package com.cmscomunidades.domain.model.valueobjects.claimsbatch;

public class EmptyClaimsBatch extends ClaimsBatch {

    public EmptyClaimsBatch() {
        super();
    }

    @Override
    public void openClaimsInCMSAsistencia() {
        System.out.println("No hay siniestros pendientes de abrir en CMS Asistencia.");
    }
}
