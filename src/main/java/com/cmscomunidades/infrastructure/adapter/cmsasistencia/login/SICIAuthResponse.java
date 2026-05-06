package com.cmscomunidades.infrastructure.adapter.cmsasistencia.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SICIAuthResponse {

    @JsonProperty("Data")
    private SICIData data;

    @JsonProperty("Success")
    private Success success;

    @JsonProperty("Code")
    private String code;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SICIData {

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private Long expiresIn;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Success {

        private String mensaje;

    }
}
