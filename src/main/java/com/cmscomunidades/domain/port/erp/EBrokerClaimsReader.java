package com.cmscomunidades.domain.port.erp;

import com.cmscomunidades.domain.model.valueobjects.erpclaimid.ERPClaimId;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.claim.EBrokerClaimDto;

import java.util.List;

public interface EBrokerClaimsReader {
    List<EBrokerClaimDto> findByIdGreaterThan(String authorizationHeader, ERPClaimId ERPClaimId);
}
