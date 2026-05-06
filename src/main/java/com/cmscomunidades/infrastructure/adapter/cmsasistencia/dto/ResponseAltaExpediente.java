package com.cmscomunidades.infrastructure.adapter.cmsasistencia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAltaExpediente {

    @JsonProperty("Data")
    private String data;

    @JsonProperty("Success")
    private Success success;

    @JsonProperty("Error")
    private Error error;

    @JsonProperty("Code")
    private Long code;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Success {
        private String mensaje;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        private String mensaje;
    }
}
