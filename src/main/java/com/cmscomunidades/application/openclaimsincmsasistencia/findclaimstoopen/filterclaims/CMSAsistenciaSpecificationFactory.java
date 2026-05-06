package com.cmscomunidades.application.openclaimsincmsasistencia.findclaimstoopen.filterclaims;

import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.model.valueobjects.professional.Professional;
import com.cmscomunidades.domain.model.valueobjects.professional.ProfessionalSpecification;
import com.cmscomunidades.domain.model.valueobjects.status.Status;
import com.cmscomunidades.domain.model.valueobjects.status.StatusSpecification;
import com.cmscomunidades.domain.shared.Specification;

public final class CMSAsistenciaSpecificationFactory {
    public static Specification<Claim> create() {
        StatusSpecification open = new StatusSpecification(Status.OPEN);
        ProfessionalSpecification cmsAsistenciaProfessional = new ProfessionalSpecification(Professional.CMS_ASISTENCIA);
        return open.and(cmsAsistenciaProfessional);
    }
}
