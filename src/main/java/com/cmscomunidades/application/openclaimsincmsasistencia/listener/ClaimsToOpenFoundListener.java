package com.cmscomunidades.application.openclaimsincmsasistencia.listener;

import com.cmscomunidades.application.openclaimsincmsasistencia.event.ClaimsToOpenFound;
import com.cmscomunidades.domain.shared.Listener;

public class ClaimsToOpenFoundListener implements Listener<ClaimsToOpenFound> {
    @Override
    public void onEvent(ClaimsToOpenFound event) {
        event.claimsBatch().openClaimsInCMSAsistencia();
    }
}
