package com.cmscomunidades.infrastructure.adapter.cmsasistencia.login;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service("authServiceSICI")
public class SICIAuthService {

    private final WebClient.Builder webClientBuilder;
    private final String siciBaseUrl;

    public SICIAuthService(WebClient.Builder webClientBuilder,
                           @Value("${cms.login.sici.url}") String siciBaseUrl) {
        this.webClientBuilder = webClientBuilder;
        this.siciBaseUrl = siciBaseUrl;
    }

    public Mono<SICIAuthResponse> login(SICIAuthRequest siciAuthRequest) {

        return webClientBuilder.build()
                .post()
                .uri(siciBaseUrl.concat("/webService/SICI/ws/login"))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(siciAuthRequest)
                .retrieve()
                .bodyToMono(SICIAuthResponse.class);
    }

    /*
    public String login(SICIAuthRequest siciAuthRequest) {

        String externalTokenResponse = webClientBuilder.build()
                .post()
                .uri("https://preproduccion.sistemasici.es/webService/SICI/ws/login")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(siciAuthRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return externalTokenResponse;
    }
    */
}
