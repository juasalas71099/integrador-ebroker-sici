package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private long id;
    private String dgs;
    private String name;
    private Object[] assistancePhones;
    private String abbreviatedName;
    private String administrativeID;
}
