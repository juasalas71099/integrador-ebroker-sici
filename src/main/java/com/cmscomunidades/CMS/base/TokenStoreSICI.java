package com.cmscomunidades.CMS.base;

import org.springframework.stereotype.Component;

@Component
public class TokenStoreSICI {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
