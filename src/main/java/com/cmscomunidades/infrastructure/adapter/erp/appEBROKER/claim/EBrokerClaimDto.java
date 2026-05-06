package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.claim;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.document.Document;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.policy.Policy;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.status.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EBrokerClaimDto {
    private long id;
    private String description;
    private Policy policy;
    private Document[] documents;
    private Status status;
    private LocalDate sinisterDate;
    private LocalDate closingDate;
    private LocalDate cancellationDate;
    private LocalDate openingDate;
    private String sinisterHour;
    private String company_reference;

    public long id() {
        return id;
    }
}