package com.cmscomunidades.infrastructure.adapter.cmsasistencia;

import com.cmscomunidades.infrastructure.adapter.cmsasistencia.dto.Expediente;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class ExpedienteService {

    private final WebClient webClient;

    public ExpedienteService(@Value("${cms.login.sici.url}") String siciBaseUrl) {
        this.webClient = buildInsecureWebClient(siciBaseUrl);
    }

    public Mono<String> altaExpediente(String authorizationHeader, Expediente expediente) {
        return webClient    .put()
                .uri("/altaExpediente")
                .bodyValue(expediente)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToMono(String.class);
    }

    private WebClient buildInsecureWebClient(String siciBaseUrl) {
        try {
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .secure(sslSpec -> sslSpec.sslContext(sslContext));

            return WebClient.builder()
                    .baseUrl(siciBaseUrl.concat("/webService/SICI/ws"))
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to initialize insecure SICI Expediente WebClient SSL context", ex);
        }
    }

}
