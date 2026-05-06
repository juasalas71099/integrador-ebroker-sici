package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.policy;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.policy.commercialPlan.CommercialPlan;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.company.Company;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.customer.Customer;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.subcategory.Subcategory;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.status.Status;
import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.office.Office;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Policy {
    private long id;
    private String number;
    private String duration;
    private Status status;
    private Subcategory subcategory;
    private String risk;
    private Company company;
    private Customer customer;
    private Status product;
    private Object[] documents;
    private LocalDate effectDate;
    private LocalDate createdDate;
    private Object cancellationDate;
    private Object cancellationReason;
    private LocalDate renewalDate;
    private String saleType;
    private String paymentMethod;
    private Office chargeOffice;
    private Office managementOffice;
    private Office productionOffice;

    @JsonProperty("commercial_plan")
    private CommercialPlan[] commercialPlan;
}
