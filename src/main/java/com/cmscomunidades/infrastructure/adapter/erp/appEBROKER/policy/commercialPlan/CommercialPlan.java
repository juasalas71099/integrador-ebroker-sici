package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.policy.commercialPlan;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.phoneAccount.PhoneAccount;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.address.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommercialPlan {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("legal_id")
    private String legalID;

    @JsonProperty("signup_date")
    private LocalDate signupDate;

    @JsonProperty("cancellation_date")
    private LocalDate cancellationDate;

    @JsonProperty("address")
    private Address address;

    @JsonProperty("phone_accounts")
    private PhoneAccount[] phoneAccounts;
}
