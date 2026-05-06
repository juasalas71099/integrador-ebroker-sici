package com.cmscomunidades.infrastructure.adapter.erp;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.claim.EBrokerClaimDto;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.customer.Customer;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.involved.Involved;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.professional.Professional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CustomerService {

    private final WebClient webClient;

    public CustomerService(@Value("${cms.login.ebroker.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<Customer> getCustomer(String authorizationHeader, Long id) {
        return webClient.get()
                .uri("/erp-crm-services/v1/customers/".concat(id.toString()))
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToMono(Customer.class);
    }



}

