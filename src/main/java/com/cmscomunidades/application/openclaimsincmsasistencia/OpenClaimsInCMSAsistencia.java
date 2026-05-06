package com.cmscomunidades.application.openclaimsincmsasistencia;

import com.cmscomunidades.CMS.base.TokenStoreEbroker;
import com.cmscomunidades.CMS.base.TokenStoreSICI;
import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.port.repository.ClaimRepository;
import com.cmscomunidades.infrastructure.adapter.cmsasistencia.ExpedienteService;
import com.cmscomunidades.infrastructure.adapter.cmsasistencia.dto.Expediente;
import com.cmscomunidades.infrastructure.adapter.cmsasistencia.dto.ResponseAltaExpediente;
import com.cmscomunidades.infrastructure.adapter.cmsasistencia.login.SICIAuthRequest;
import com.cmscomunidades.infrastructure.adapter.cmsasistencia.login.SICIAuthResponse;
import com.cmscomunidades.infrastructure.adapter.cmsasistencia.login.SICIAuthService;
import com.cmscomunidades.infrastructure.adapter.erp.ClaimService;
import com.cmscomunidades.infrastructure.adapter.erp.CommercialPlanService;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.claim.EBrokerClaimDto;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.policy.commercialPlan.CommercialPlan;
import com.cmscomunidades.infrastructure.adapter.repository.ClaimBD;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.phoneAccount.PhoneAccount;

@Service
public class OpenClaimsInCMSAsistencia {

    private final TokenStoreEbroker tokenStoreEbroker;
    private final TokenStoreSICI tokenStoreSICI;
    private final ClaimRepository claimRepository;
    private final ClaimService claimService;
    private final ExpedienteService expedienteService;
    private final SICIAuthService siciAuthService;
    private final String activeProfile;
    private final CommercialPlanService commercialPlanService;

    private List<Claim> pendingClaims = new ArrayList<Claim>();

    public OpenClaimsInCMSAsistencia(TokenStoreEbroker tokenStoreEbroker,
                                     ClaimRepository claimRepository,
                                     ClaimService claimService,
                                     ExpedienteService expedienteService,
                                     TokenStoreSICI tokenStoreSICI,
                                     SICIAuthService siciAuthService,
                                     CommercialPlanService commercialPlanService,
                                     @Value("${spring.profiles.active:}") String activeProfile) {
        this.tokenStoreEbroker = tokenStoreEbroker;
        this.claimRepository = claimRepository;
        this.claimService = claimService;
        this.expedienteService = expedienteService;
        this.tokenStoreSICI = tokenStoreSICI;
        this.siciAuthService = siciAuthService;
        this.activeProfile = activeProfile;
        this.commercialPlanService = commercialPlanService;
    }

    public void execute() {
        System.out.println("Ejecutando openClaimsInCMSAsistencia con token " + tokenStoreEbroker.getToken());

        Long lastEbrokerId = getLastClaim()
                .map(ClaimBD::getEbroker_id)
                .filter(Objects::nonNull)
                .orElse(0L);

        System.out.println("Ultimo id en BD: " + lastEbrokerId);

        getNewClaims(lastEbrokerId)
                .flatMapMany(Flux::fromIterable)
                .flatMap(claim -> isCMSAsistencia(claim)
                        .filter(Boolean::booleanValue)
                        .map(valid -> claim))
                .doOnNext(claim -> System.out.println(
                        "Claim con id > " + lastEbrokerId
                                + " y profesional CMS ASISTENCIA -> id: " + claim.getId()
                                + ", status: " + (claim.getStatus() != null ? claim.getStatus().getId() : "N/A")
                                + ", referencia: " + claim.getCompany_reference()
                ))
                .filter(claim -> claim.getStatus() != null && "A".equals(claim.getStatus().getId()))
                .doOnNext(claim -> System.out.println(
                        "Siniestro filtrado -> id: " + claim.getId()
                                + ", status: " + claim.getStatus().getId()
                                + ", profesional: CMS ASISTENCIA"
                                + ", referencia: " + claim.getCompany_reference()
                                + ", poliza: " + (claim.getPolicy() != null ? claim.getPolicy().getNumber() : "N/A")
                ))
                .flatMap(this::processClaimSafely)
                .doOnError(e -> {
                    System.out.println("Error global en OpenClaimsInCMSAsistencia");
                    e.printStackTrace();
                })
                .subscribe();
    }

    public Optional<ClaimBD> getLastClaim() {
        return this.claimRepository.findTopByOrderByIdDesc();
    }

    public Mono<List<EBrokerClaimDto>> getNewClaims(Long lastEbrokerId) {
        return this.claimService.getClaimsWithIdGreaterThan("Bearer ".concat(tokenStoreEbroker.getToken()), lastEbrokerId);
    }

    public Mono<Boolean> isCMSAsistencia(EBrokerClaimDto claim) {
        return claimService
                .getProfessionals("Bearer ".concat(tokenStoreEbroker.getToken()), claim.getId())
                .defaultIfEmpty(Collections.emptyList())
                .map(professionals -> professionals.stream()
                        .anyMatch(professional ->
                                "CMS ASISTENCIA".equalsIgnoreCase(professional.getComplete_name().trim()))
                );
    }

    public Mono<CommercialPlan> getCommercialPlanData(Long commercialPlanId) {
        return this.commercialPlanService.getCommercialPlanData("Bearer ".concat(tokenStoreEbroker.getToken()), commercialPlanId);
    }

    public Mono<Expediente> createExpedienteFromClaim(EBrokerClaimDto claim) {
        if (claim == null
                || claim.getPolicy() == null
                || claim.getPolicy().getCustomer() == null
                || claim.getPolicy().getCustomer().getAddress() == null) {
            return Mono.error(new IllegalStateException("Claim invalida para crear expediente: faltan policy/customer/address"));
        }

        Expediente expediente = new Expediente();
        expediente.setNombreApellidos(claim.getPolicy().getCustomer().getCompleteName());
        expediente.setNroServicio("2"); //TODO: PREGUNTAR QUE CAMPO DE EBROKER VA AQUI, cambiar cada registro nuevo que vayamos a hacer (no admite duplicados)
        expediente.setNroEncargo(claim.getCompany_reference());
        expediente.setNif(claim.getPolicy().getCustomer().getLegalID());
        expediente.setDireccion(claim.getPolicy().getCustomer().getAddress().getDescription());

        if (claim.getPolicy().getCustomer().getAddress().getPostalCode() != null) {
            expediente.setCp(Long.parseLong(claim.getPolicy().getCustomer().getAddress().getPostalCode()));
        }

        expediente.setLocalidad(claim.getPolicy().getCustomer().getAddress().getTown().concat(" " + claim.getPolicy().getCustomer().getAddress().getCity()));
        expediente.setProvincia(claim.getPolicy().getCustomer().getAddress().getProvince());


        if (claim.getDescription() != null) {
            expediente.setObservaciones("Compania: "
                    .concat(claim.getPolicy().getCompany().getName())
                    .concat("\n\n")
                    .concat(claim.getDescription()));
        } else {
            expediente.setObservaciones("Compania: ".concat(claim.getPolicy().getCompany().getName()));
        }

        expediente.setPoliza(claim.getPolicy().getNumber());

        if (claim.getPolicy() == null
                || claim.getPolicy().getCommercialPlan() == null
                || claim.getPolicy().getCommercialPlan().length == 0
                || claim.getPolicy().getCommercialPlan()[0] == null) {
            System.out.println("Claim " + claim.getId() + " sin commercial plan. Se enviara expediente sin email.");
            return Mono.just(expediente);
        }

        Long commercialPlanId = claim.getPolicy().getCommercialPlan()[0].getId();
        if (commercialPlanId <= 0) {
            System.out.println("Claim " + claim.getId() + " con commercialPlanId invalido (" + commercialPlanId + "). Se enviara expediente sin email.");
            return Mono.just(expediente);
        }

        return getCommercialPlanData(commercialPlanId)
                .defaultIfEmpty(new CommercialPlan())
                .map(commercialPlan -> {
                    String emailAAFF = extractMailAaff(commercialPlan);
                    String telefonoAAFF = extractTelefonoAaff(commercialPlan);
                    if (emailAAFF != null) {
                        expediente.setEmail(emailAAFF);
                        System.out.println("Email obtenido de commercial plan " + commercialPlanId + " (MAIL AAFF): " + emailAAFF);
                    } else {
                        System.out.println("Commercial plan " + commercialPlanId + " sin MAIL AAFF. Se enviara expediente sin email.");
                    }

                    if (telefonoAAFF != null) {
                        expediente.setTelefono2(telefonoAAFF);
                        System.out.println("Telefono obtenido de commercial plan " + commercialPlanId + " (TELEFONO AAFF): " + telefonoAAFF);
                    } else {
                        System.out.println("Commercial plan " + commercialPlanId + " sin TELEFONO AAFF. Se enviara expediente sin telefono1.");
                    }
                    return expediente;
                })
                .onErrorResume(e -> {
                    System.out.println("Error obteniendo commercial plan " + commercialPlanId + " para claim " + claim.getId());
                    e.printStackTrace();
                    return Mono.just(expediente);
                });
    }

    private String extractMailAaff(CommercialPlan commercialPlan) {
        if (commercialPlan == null || commercialPlan.getPhoneAccounts() == null) {
            return null;
        }

        for (PhoneAccount phoneAccount : commercialPlan.getPhoneAccounts()) {
            if (phoneAccount == null || phoneAccount.getDescription() == null || phoneAccount.getPhone() == null) {
                continue;
            }

            String description = phoneAccount.getDescription().trim();
            String value = phoneAccount.getPhone().trim();
            if ("MAIL AAFF".equalsIgnoreCase(description) && !value.isEmpty()) {
                return value;
            }
        }

        return null;
    }

    private String extractTelefonoAaff(CommercialPlan commercialPlan) {
        if (commercialPlan == null || commercialPlan.getPhoneAccounts() == null) {
            return null;
        }

        for (PhoneAccount phoneAccount : commercialPlan.getPhoneAccounts()) {
            if (phoneAccount == null || phoneAccount.getDescription() == null || phoneAccount.getPhone() == null) {
                continue;
            }

            String description = phoneAccount.getDescription().trim();
            String value = phoneAccount.getPhone().trim();
            if ("TELEFONO AAFF".equalsIgnoreCase(description) && !value.isEmpty()) {
                return value;
            }
        }

        return null;
    }

    private Mono<Void> processClaimSafely(EBrokerClaimDto claim) {
        return claimService.getInvolved("Bearer ".concat(tokenStoreEbroker.getToken()), claim.getId())
                .doOnNext(System.out::println)
                .onErrorResume(e -> {
                    System.out.println("No se pudieron obtener involucrados para claim " + claim.getId());
                    e.printStackTrace();
                    return Mono.empty();
                })
                .then(loginSici(claim))
                .onErrorResume(e -> {
                    System.out.println("Error procesando claim " + claim.getId());
                    e.printStackTrace();
                    return Mono.empty();
                });
    }

    private Mono<Void> loginSici(EBrokerClaimDto eBrokerClaim) {
        try {
            if (isTestProfile()) {
                SICIAuthRequest testRequest = new SICIAuthRequest();
                testRequest.setUsuario("cms");
                testRequest.setClave("cms01");
                testRequest.setEmpresa("65473215");

                return siciAuthService.login(testRequest)
                        .doOnNext(this::setSiciToken)
                        .flatMap(res -> altaExpedienteSici(eBrokerClaim));
            }

            Map<String, SICIAuthRequest> siciAuthRequestDictionary = loadSICIAuthRequestsFromResources();

            if (eBrokerClaim.getPolicy() == null
                    || eBrokerClaim.getPolicy().getCommercialPlan() == null
                    || eBrokerClaim.getPolicy().getCommercialPlan().length == 0
                    || eBrokerClaim.getPolicy().getCommercialPlan()[0] == null
                    || eBrokerClaim.getPolicy().getCommercialPlan()[0].getName() == null) {
                return Mono.error(new IllegalStateException(
                        "Claim " + eBrokerClaim.getId() + " sin CommercialPlan->Name para login SICI"));
            }

            String eBrokerClaimAAFF = eBrokerClaim.getPolicy().getCommercialPlan()[0].getName();
            SICIAuthRequest validSiciAuthRequest = siciAuthRequestDictionary.get(eBrokerClaimAAFF);
            if (validSiciAuthRequest == null) {
                return Mono.error(new IllegalStateException(
                        "No existe credencial SICI para AAFF '" + eBrokerClaimAAFF + "' en loginAAFF.json"));
            }

            return siciAuthService.login(validSiciAuthRequest)
                    .doOnNext(this::setSiciToken)
                    .flatMap(res -> altaExpedienteSici(eBrokerClaim));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private boolean isTestProfile() {
        return activeProfile != null && activeProfile.toLowerCase().contains("test");
    }

    public static Map<String, SICIAuthRequest> loadSICIAuthRequestsFromResources() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = SICIAuthLoader.class
                .getClassLoader()
                .getResourceAsStream("loginAAFF.json")) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Archivo loginAAFF.json no encontrado en resources.");
            }

            return objectMapper.readValue(
                    inputStream,
                    new TypeReference<Map<String, SICIAuthRequest>>() {}
            );
        }
    }

    private void setSiciToken(SICIAuthResponse res) {
        if (res.getData() != null && !res.getData().getAccessToken().isEmpty()) {
            tokenStoreSICI.setToken(res.getData().getAccessToken());
            System.out.println("Token de SICI obtenido. Expira en " + res.getData().getExpiresIn() + "ms");
        }
    }

    private Mono<Void> altaExpedienteSici(EBrokerClaimDto claim) {
        ObjectMapper mapper = new ObjectMapper();
        return createExpedienteFromClaim(claim)
                .flatMap(expediente -> this.expedienteService.altaExpediente(tokenStoreSICI.getToken(), expediente))
                .flatMap(body -> {
                    try {
                        ResponseAltaExpediente response = mapper.readValue(body, ResponseAltaExpediente.class);
                        System.out.println("Respuesta de la API de SICI: " + response);

                        ClaimBD claimBD = new ClaimBD();
                        claimBD.setEbroker_id(claim.getId());

                        ClaimBD createdClaim = this.claimRepository.save(claimBD);
                        System.out.println("Ultimo registro guardado con id: " + createdClaim.getEbroker_id());
                        return Mono.<Void>empty();
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }
}
