package com.cmscomunidades.domain.port.cmsasistencia;

import com.cmscomunidades.domain.model.claim.Claim;
import org.springframework.stereotype.Component;

@Component
public interface SendClaim {
    void send(String authorizationHeader, Claim claim);
}
