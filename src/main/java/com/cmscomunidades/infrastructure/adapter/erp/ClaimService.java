package com.cmscomunidades.infrastructure.adapter.erp;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.involved.Involved;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.professional.Professional;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.claim.EBrokerClaimDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ClaimService {

    private final WebClient webClient;

    public ClaimService(@Value("${cms.login.ebroker.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<List<EBrokerClaimDto>> getClaims(String authorizationHeader) {
        return webClient.get()
                .uri("/erp-business-services/v1/claims")
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToFlux(EBrokerClaimDto.class)
                .collectList();
    }

    public Mono<List<Professional>> getProfessionals(String authorizationHeader, Long id) {
        return webClient.get()
                .uri("/erp-business-services/v1/claims/".concat(id.toString()).concat("/professionals"))
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToFlux(Professional.class)
                .collectList();
    }

    public Mono<List<EBrokerClaimDto>> getClaimsWithIdGreaterThan(String authorizationHeader, Long gt) {
        return webClient.get()
                .uri("/erp-business-services/v1/claims?query=id>".concat(gt.toString()))
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToFlux(EBrokerClaimDto.class)
                .collectList();
    }

    public Mono<List<Involved>> getInvolved(String authorizationHeader, Long id) {
        return webClient.get()
                .uri("/erp-business-services/v1/claims/".concat(id.toString()).concat("/involved"))
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToFlux(Involved.class)
                .collectList();
    }

    public Mono<List<EBrokerClaimDto>> getClaimById(String authorizationHeader, Long id) {
        return webClient.get()
                .uri("/erp-business-services/v1/claims/".concat(id.toString()))
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToFlux(EBrokerClaimDto.class)
                .collectList();
    }

}

