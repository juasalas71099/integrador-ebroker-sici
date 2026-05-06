package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String description;
    private String city;
    private String town;
    private String province;
    private String postalCode;

}