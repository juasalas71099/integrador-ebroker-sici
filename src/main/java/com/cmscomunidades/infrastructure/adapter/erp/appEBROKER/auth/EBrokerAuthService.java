package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service("authServiceEBROKER")
public class EBrokerAuthService {

    private final WebClient.Builder webClientBuilder;

    @Value("${cms.login.ebroker.urlToken}")
    private String url;

    public EBrokerAuthService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /*
    public String login(EBrokerAuthRequest EBrokerAuthRequest) {

        String externalTokenResponse = webClientBuilder.build()
                .post()
                .uri("https://pre-sso.ebroker.es/realms/30200382/protocol/openid-connect/token")
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("client_id", EBrokerAuthRequest.getClientId())
                        .with("username", EBrokerAuthRequest.getUsername())
                        .with("password", EBrokerAuthRequest.getPassword())
                        .with("grant_type", EBrokerAuthRequest.getGrantType()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return externalTokenResponse;
    }*/

    public EBrokerAuthResponse login(EBrokerAuthRequest EBrokerAuthRequest) {

        EBrokerAuthResponse externalTokenResponse = webClientBuilder.build()
                .post()
                .uri(url)
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("client_id", EBrokerAuthRequest.getClientId())
                        .with("username", EBrokerAuthRequest.getUsername())
                        .with("password", EBrokerAuthRequest.getPassword())
                        .with("grant_type", EBrokerAuthRequest.getGrantType()))
                .retrieve()
                .bodyToMono(EBrokerAuthResponse.class)
                .block();

        return externalTokenResponse;
    }
}
