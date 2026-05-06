package com.cmscomunidades.CMS;

import com.cmscomunidades.infrastructure.adapter.cmsasistencia.login.SICIAuthRequest;
import com.cmscomunidades.infrastructure.adapter.cmsasistencia.login.SICIAuthResponse;
import com.cmscomunidades.infrastructure.adapter.cmsasistencia.login.SICIAuthService;
import com.cmscomunidades.CMS.base.TokenStoreEbroker;
import com.cmscomunidades.CMS.base.TokenStoreSICI;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.auth.EBrokerAuthRequest;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.auth.EBrokerAuthResponse;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.auth.EBrokerAuthService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StartupService {

    private final EBrokerAuthService eBrokerAuthService;
    private final TokenStoreEbroker tokenStoreEbroker;

    @Value("${cms.login.ebroker.username}")
    private String username;

    @Value("${cms.login.ebroker.password}")
    private String password;

    public StartupService(EBrokerAuthService eBrokerAuthService, TokenStoreEbroker tokenStoreEbroker) {
        this.eBrokerAuthService = eBrokerAuthService;
        this.tokenStoreEbroker = tokenStoreEbroker;
    }

    @PostConstruct
    @Scheduled(fixedRateString = "${cms.scheduler.openclaimsincmsasistencia.interval}")
    public void initEbroker() {

        //OBJETO LOGIN EBROKER
        EBrokerAuthRequest eBrokerAuthRequest = new EBrokerAuthRequest();
        eBrokerAuthRequest.setClientId("erp-business-services");
        eBrokerAuthRequest.setGrantType("password");
        eBrokerAuthRequest.setUsername(username);
        eBrokerAuthRequest.setPassword(password);

        //LOGIN EBROKER
        EBrokerAuthResponse eBrokerLoginResponse = eBrokerAuthService.login(eBrokerAuthRequest);
        try {
            if(eBrokerLoginResponse.getAccessToken() != null && !eBrokerLoginResponse.getAccessToken().isEmpty()){
                tokenStoreEbroker.setToken(eBrokerLoginResponse.getAccessToken());
                System.out.println("Token de eBroker obtenido al iniciar: " + tokenStoreEbroker.getToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
