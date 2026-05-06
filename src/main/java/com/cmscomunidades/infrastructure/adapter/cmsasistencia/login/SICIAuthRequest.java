package com.cmscomunidades.infrastructure.adapter.cmsasistencia.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SICIAuthRequest {

    private String empresa;
    private String usuario;
    private String clave;

}
