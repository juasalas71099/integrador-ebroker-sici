package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


@RestController("loginControllerEBROKER")
@RequestMapping("/loginEBROKER")
@RequiredArgsConstructor
public class EBrokerLoginController {

    private final EBrokerAuthService EBrokerAuthService;
    private final WebClient.Builder webClientBuilder;

    @PostMapping("/login")
    public ResponseEntity<EBrokerAuthResponse> login(@RequestBody EBrokerAuthRequest EBrokerAuthRequest) {
        EBrokerAuthResponse token = EBrokerAuthService.login(EBrokerAuthRequest);

        return ResponseEntity.ok(token);
    }
}
