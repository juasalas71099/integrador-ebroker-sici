package com.cmscomunidades.infrastructure.adapter.cmsasistencia.login;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController("loginControllerSICI")
@RequestMapping("/loginSICI")
@RequiredArgsConstructor
public class SICILoginController {

    private final SICIAuthService SICIAuthService;
    private final WebClient.Builder webClientBuilder;


//    @PostMapping("/login")
//    public ResponseEntity<SICIAuthResponse> login(@RequestBody SICIAuthRequest siciAuthRequest) {
//        SICIAuthResponse token = SICIAuthService.login(siciAuthRequest);
//
//        return ResponseEntity.ok(token);
//    }
}
