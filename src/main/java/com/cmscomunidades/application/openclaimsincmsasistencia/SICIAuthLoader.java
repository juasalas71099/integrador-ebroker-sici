package com.cmscomunidades.application.openclaimsincmsasistencia;

import com.cmscomunidades.infrastructure.adapter.cmsasistencia.login.SICIAuthRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SICIAuthLoader {

    public static Map<String, SICIAuthRequest> loadFromJson(String resourcePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = SICIAuthLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IOException("Archivo JSON no encontrado: " + resourcePath);
            }
            return mapper.readValue(input,
                    mapper.getTypeFactory().constructMapType(Map.class, String.class, SICIAuthRequest.class));
        }
    }

}