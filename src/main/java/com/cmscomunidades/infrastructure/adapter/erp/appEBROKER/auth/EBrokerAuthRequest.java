package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EBrokerAuthRequest {

    private String grantType;
    private String clientId;
    private String username;
    private String password;

}
