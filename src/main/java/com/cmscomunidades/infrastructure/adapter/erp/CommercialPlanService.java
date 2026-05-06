package com.cmscomunidades.infrastructure.adapter.erp;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.policy.commercialPlan.CommercialPlan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CommercialPlanService {

    private final WebClient webClient;

    public CommercialPlanService(@Value("${cms.login.ebroker.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<CommercialPlan> getCommercialPlanData(String authorizationHeader, Long id) {
        System.out.println("Consultando commercial entity en eBroker con id: " + id);
        return webClient.get()
                .uri("/erp-business-services/v1/commercial-entities/".concat(id.toString()))
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToMono(CommercialPlan.class);
    }


}

