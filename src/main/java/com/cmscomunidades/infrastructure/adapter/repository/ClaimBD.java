package com.cmscomunidades.infrastructure.adapter.repository;
import com.cmscomunidades.CMS.base.Entidad;
import com.cmscomunidades.domain.model.claim.Claim;
import com.cmscomunidades.domain.model.claim.ClaimBuilder;
import com.cmscomunidades.domain.model.valueobjects.claimid.ClaimId;
import com.cmscomunidades.domain.model.valueobjects.erpclaimid.ERPClaimId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name="CLAIM")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClaimBD implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "ebroker_id")
    private Long ebroker_id;

    @Column(name = "status")
    private String status;

    public Claim toDomain() {
        return new ClaimBuilder()
                .withClaimId(new ClaimId(id))
                .withEBrokerId(new ERPClaimId(ebroker_id))
                .build();
    }
}
