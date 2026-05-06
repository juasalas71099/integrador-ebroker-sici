package com.cmscomunidades.application.openclaimsincmsasistencia;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OpenClaimsInCMSAsistenciaScheduledJob {

    private final OpenClaimsInCMSAsistencia openClaimsInCMSAsistencia;

    public OpenClaimsInCMSAsistenciaScheduledJob(OpenClaimsInCMSAsistencia openClaimsInCMSAsistencia) {
        this.openClaimsInCMSAsistencia = openClaimsInCMSAsistencia;
    }

    @Scheduled(fixedRateString = "${cms.scheduler.openclaimsincmsasistencia.interval}")
    public void run() {
        openClaimsInCMSAsistencia.execute();
    }
}