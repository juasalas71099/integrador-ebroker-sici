package com.cmscomunidades.presentation;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.claim.EBrokerClaimDto;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.professional.Professional;
import com.cmscomunidades.infrastructure.adapter.erp.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/claims")
public class ClaimController {

    private final ClaimService claimService;

    @Autowired
    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping("/claims")
    public Mono<List<EBrokerClaimDto>> getClaims(@RequestHeader("Authorization") String authorizationHeader) {
        return claimService.getClaims(authorizationHeader);
    }

    @GetMapping("professionals/{id}")
    public Mono<List<Professional>> getProfessionals(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") Long id) {
        return claimService.getProfessionals(authorizationHeader, id);
    }
}
