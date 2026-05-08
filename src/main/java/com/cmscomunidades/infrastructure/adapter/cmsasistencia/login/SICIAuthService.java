package com.cmscomunidades.infrastructure.adapter.cmsasistencia.login;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.core.publisher.Mono;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

@Service("authServiceSICI")
public class SICIAuthService {

    private final WebClient webClient;
    private final String siciBaseUrl;

    public SICIAuthService(WebClient.Builder webClientBuilder,
                           @Value("${cms.login.sici.url}") String siciBaseUrl) {
        this.webClient = buildSiciWebClient(webClientBuilder);
        this.siciBaseUrl = siciBaseUrl;
    }

    public Mono<SICIAuthResponse> login(SICIAuthRequest siciAuthRequest) {

        return webClient
                .post()
                .uri(siciBaseUrl.concat("/webService/SICI/ws/login"))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(siciAuthRequest)
                .retrieve()
                .bodyToMono(SICIAuthResponse.class);
    }

    private WebClient buildSiciWebClient(WebClient.Builder webClientBuilder) {
        try {
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .secure(sslSpec -> sslSpec.sslContext(sslContext));

            return webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to initialize insecure SICI WebClient SSL context", ex);
        }
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
