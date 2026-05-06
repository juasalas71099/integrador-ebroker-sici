package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.customer;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.segmentationLevel.SegmentationLevel;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.address.Address;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.office.Office;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private long id;

    @JsonProperty("legal_id")
    private String legalID;

    private String name;
    private String surname1;
    private Object surname2;
    private String completeName;
    private String phone;
    private String email;
    private Address address;
    private Object birthDate;
    private LocalDate signupDate;
    private Object cancellationDate;
    private Object cancellationReason;
    private SegmentationLevel segmentationLevel;
    private Object[] bankAccounts;
    private Object[] phoneAccounts;
    private Object[] emailAccounts;
    private boolean linkedPhoto;
    private Office chargeOffice;
    private Office managementOffice;
    private Office productionOffice;

}

