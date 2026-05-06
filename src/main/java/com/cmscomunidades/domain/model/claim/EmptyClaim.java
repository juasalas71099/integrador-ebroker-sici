package com.cmscomunidades.domain.model.claim;

public class EmptyClaim extends Claim {

    @Override
    public void openClaimInCMSAsistencia() {
        System.out.println("No hay siniestros pendientes de abrir en CMS Asistencia.");
    }

    @Override
    public String toString() {
        return "EmpyClaim{}";
    }
}
