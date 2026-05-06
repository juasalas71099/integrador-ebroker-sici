package com.cmscomunidades.infrastructure.adapter.cmsasistencia.sendclaim;

import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.port.cmsasistencia.SendClaim;

public class EmptySendClaim implements SendClaim {
    @Override
    public void send(String authorizationHeader, Claim claim) {
        throw new UnsupportedOperationException("SendClaim operation is not supported in EmptySendClaim");
    }
}
