package com.cmscomunidades.application.openclaimsincmsasistencia;

import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.model.claim.ClaimBuilder;
import com.cmscomunidades.domain.model.claim.ClaimFactory;
import com.cmscomunidades.domain.model.claim.EmptyClaim;
import com.cmscomunidades.domain.model.valueobjects.claimsbatch.ClaimsBatchFactory;
import com.cmscomunidades.domain.model.valueobjects.professional.Professional;
import com.cmscomunidades.domain.model.valueobjects.status.Status;
import com.cmscomunidades.domain.port.cmsasistencia.SendClaim;
import com.cmscomunidades.domain.port.erp.FindNewClaimsInERP;
import com.cmscomunidades.domain.port.repository.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class OpenClaimsTest {

    private FindNewClaimsInERP mockFindNewClaimsInERP;
    private ClaimRepository mockRepository;
    private OpenClaims openClaims;
    private SendClaim mockSendClaim;
    private ClaimBuilder aClaimBuilder;
    private ClaimsBatchFactory claimsBatchFactory;

    @BeforeEach
    public void setUp() {
        mockDependencies();
        openClaims = new OpenClaims(
                mockFindNewClaimsInERP,
                mockRepository
        );
        aClaimBuilder = ClaimFactory.aClaimBuilder().withSendClaim(mockSendClaim);
        claimsBatchFactory = new ClaimsBatchFactory();
    }

    private void mockDependencies() {
        mockFindNewClaimsInERP = mock(FindNewClaimsInERP.class);
        mockRepository = mock(ClaimRepository.class);
        mockSendClaim = mock(SendClaim.class);
    }

    @Test public void
    dont_open_claims_in_cms_asistencia_when_no_claims_in_erp() {
        testDontOpenClaimsWhen(new EmptyClaim());
    }

    @Test public void
    dont_open_claims_in_cms_asistencia_when_one_claim_but_not_for_cms_asistencia() {
        // Arrange
        aClaimBuilder
                .withProfessional(Professional.OTHER)
                .withStatus(Status.OPEN);

        // Act & Assert
        testDontOpenClaimsWhen(aClaimBuilder.build());
    }

    @Test public void
    dont_open_claims_in_cms_asistencia_when_one_claim_but_not_open() {
        // Arrange
        aClaimBuilder
                .withProfessional(Professional.CMS_ASISTENCIA)
                .withStatus(Status.CLOSED);

        // Act & Assert
        testDontOpenClaimsWhen(aClaimBuilder.build());
    }

    @Test public void
    open_claims_in_cms_asistencia_when_one_claim_for_cms_asistencia() {
        // Arrange
        aClaimBuilder
                .withSendClaim(mockSendClaim)
                .withProfessional(Professional.CMS_ASISTENCIA)
                .withStatus(Status.OPEN);

        Claim aClaim = aClaimBuilder.build();
        when(mockFindNewClaimsInERP
                .withIdGreaterThan(any()))
                .thenReturn(claimsBatchFactory.of(List.of(aClaim)));

        // Act
        openClaims.execute();

        // Assert
        verify(mockSendClaim, times(1)).send(null, aClaim);
    }

    //todo test when two claims but only one for cms cms asistencia

    private void testDontOpenClaimsWhen(Claim claim) {
        System.out.println("TESTING DO NOT OPEN CLAIMS WHEN: " + claim);

        // Arrange
        when(mockFindNewClaimsInERP
                .withIdGreaterThan(any()))
                .thenReturn(claimsBatchFactory.of(List.of(claim)));

        // Act
        openClaims.execute();

        // Assert
        verify(mockFindNewClaimsInERP, times(1)).withIdGreaterThan(any());
        verifyNoInteractions(mockSendClaim);
    }


}
