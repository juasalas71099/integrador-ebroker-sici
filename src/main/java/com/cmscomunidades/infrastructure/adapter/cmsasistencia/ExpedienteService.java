package com.cmscomunidades.infrastructure.adapter.cmsasistencia;

import com.cmscomunidades.infrastructure.adapter.cmsasistencia.dto.Expediente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExpedienteService {

    private final WebClient webClient;

    public ExpedienteService(@Value("${cms.login.sici.url}") String siciBaseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(siciBaseUrl.concat("/webService/SICI/ws"))
                .build();
    }

    public Mono<String> altaExpediente(String authorizationHeader, Expediente expediente) {
        return webClient    .put()
                .uri("/altaExpediente")
                .bodyValue(expediente)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToMono(String.class);
    }

}
